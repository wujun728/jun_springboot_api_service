package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class AddRepositoryAnnotationPlugin extends PluginAdapter
{
  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
    interfaze.addAnnotation("@Repository");

    return true;
  }

  public boolean validate(List<String> warnings)
  {
    return true;
  }
}
