package org.harmony.toddler.mybatis.groovy;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

public class GroovyLangDriver implements LanguageDriver {

    public GroovyLangDriver() {
        this(GroovyLangDriverConfig.newInstance());
    }

    public GroovyLangDriver(GroovyLangDriverConfig config) {
        GroovyFacade.initialize(config);
    }

    @Override
    public ParameterHandler createParameterHandler(
            MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterTypeClass) {
        String spt = script.getNode().getTextContent();
        if (spt == null || spt.trim().length() == 0) {
            spt = script.getStringAttribute("id");
        }
        return new GroovySqlSource(configuration, spt, parameterTypeClass == null ? Object.class : parameterTypeClass);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterTypeClass) {
        return new GroovySqlSource(configuration, script, parameterTypeClass == null ? Object.class : parameterTypeClass);
    }
}
