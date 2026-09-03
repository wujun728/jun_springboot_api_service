package com.gitee.sqlrest.core.exec.engine.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.core.dto.ScriptEditorCompletion;
import com.gitee.sqlrest.core.exec.annotation.Module;
import com.gitee.sqlrest.core.exec.engine.AbstractExecutorEngine;
import com.gitee.sqlrest.core.exec.module.DbVarModule;
import com.gitee.sqlrest.core.exec.module.EnvVarModule;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import com.zaxxer.hikari.HikariDataSource;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;

public class ScriptExecutorService extends AbstractExecutorEngine {

  public static List<ScriptEditorCompletion> syntax = new ArrayList<>();
  public static List<Class> modules = Arrays.asList(EnvVarModule.class, DbVarModule.class);

  static {
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("foreach")
            .caption("foreach")
            .value("for(item in collection){\n\t\n}")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("for")
            .caption("fori")
            .value("for(i=0;i< ;i++){\n\t\n}")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("for")
            .caption("for")
            .value("for( ){\n\t\n}")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("if")
            .caption("if")
            .value("if( ){\n\n}")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("if")
            .caption("ifelse")
            .value("if( ){\n\t\n}else{\n\t\n}")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("import")
            .caption("import")
            .value("import ")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("continue")
            .caption("continue")
            .value("continue;")
            .build());
    syntax.add(
        ScriptEditorCompletion.builder()
            .meta("break")
            .caption("break")
            .value("break;")
            .build());
  }

  public ScriptExecutorService(HikariDataSource dataSource, ProductTypeEnum productType) {
    super(dataSource, productType);
  }

  public static String getModuleVarName(Class clazz) {
    if (clazz.isAnnotationPresent(Module.class)) {
      Module annotation = (Module) clazz.getAnnotation(Module.class);
      return annotation.value();
    }
    return "unknown";
  }

  @Override
  public Object execute(List<ApiContextEntity> scripts, Map<String, Object> params, NamingStrategyEnum strategy) {
    EnvVarModule envModule = SpringUtil.getBean(EnvVarModule.class);
    DbVarModule dbModule = new DbVarModule(dataSource, productType, params, strategy);

    List<Object> results = new ArrayList<>();
    for (ApiContextEntity entity : scripts) {
      Binding binding = new Binding();
      params.forEach((k, v) -> binding.setProperty(k, v));
      binding.setProperty(getModuleVarName(dbModule.getClass()), dbModule);
      binding.setProperty(getModuleVarName(entity.getClass()), envModule);

      GroovyShell groovyShell = new GroovyShell(binding);
      try {
        results.add(groovyShell.evaluate(entity.getSqlText()));
      } catch (CompilationFailedException e) {
        throw new RuntimeException(e);
      }
    }
    return results;
  }

}
