package org.harmony.toddler.mybatis.groovy;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

public class GroovySqlSource implements SqlSource {

    static final String PARAMETER_OBJECT_KEY = "_parameter";
    protected static final String DATABASE_ID_KEY = "_databaseId";
    protected static final String MAPPING_COLLECTOR_KEY = "_pmc";
    protected static final String VARIABLES_KEY = "_vars";

    private final ParameterMapping[] parameterMappingSources;
    private final Object compiledScript;
    private final Configuration configuration;
    private static int templateIndex = 0;
    private final GroovySqlBuilder sqlBuilder;
    private final Class<?> parameterType;

    public GroovySqlSource(Configuration newConfiguration, String script, Class<?> parameterTypeClass) {
        this.configuration = newConfiguration;
        this.parameterMappingSources = null;
        this.sqlBuilder = new GroovySqlBuilder(newConfiguration);
        this.parameterType = parameterTypeClass == null ? Object.class : parameterTypeClass;
        this.compiledScript = GroovyFacade.compile(script, "groovy-tpl-" + (++templateIndex));
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        final Map<String, Object> context = new HashMap<>();
        final ParameterMappingCollector pmc = new ParameterMappingCollector(this.parameterMappingSources, context,
                this.configuration);

        context.put(DATABASE_ID_KEY, this.configuration.getDatabaseId());
        context.put(PARAMETER_OBJECT_KEY, parameterObject);
        context.put(MAPPING_COLLECTOR_KEY, pmc);
        context.put(VARIABLES_KEY, this.configuration.getVariables());

        final String sql = GroovyFacade.applyFunc(this.compiledScript, context);
        SqlSource sqlSource = sqlBuilder.parse(sql, parameterType, context);

//        BoundSql boundSql = new BoundSql(this.configuration, sql, pmc.getParameterMappings(), parameterObject);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }

        return boundSql;
    }
}
