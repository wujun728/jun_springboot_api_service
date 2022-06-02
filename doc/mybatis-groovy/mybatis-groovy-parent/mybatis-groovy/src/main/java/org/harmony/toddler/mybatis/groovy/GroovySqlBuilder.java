package org.harmony.toddler.mybatis.groovy;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class GroovySqlBuilder extends BaseBuilder {

    private final SqlSourceBuilder sqlSourceBuilder;
    private Pattern injectionFilter;

    public GroovySqlBuilder(Configuration configuration) {
        this(configuration, null);
    }

    public GroovySqlBuilder(Configuration configuration, Pattern injectionFilter) {
        super(configuration);
        sqlSourceBuilder = new SqlSourceBuilder(configuration);
        this.injectionFilter = injectionFilter;
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> context) {
        String sql = parse(originalSql, context);
        return sqlSourceBuilder.parse(sql, parameterType, context);
    }

    public String parse(String originalSql, Map<String, Object> context) {
        GenericTokenParser parser = createParser(new BindingTokenParser(context, injectionFilter));
        return parser.parse(originalSql);
    }

    private GenericTokenParser createParser(TokenHandler handler) {
        return new GenericTokenParser("${", "}", handler);
    }

    private static class BindingTokenParser implements TokenHandler {

        private Map<String, Object> context;
        private Pattern injectionFilter;

        public BindingTokenParser(Map<String, Object> context, Pattern injectionFilter) {
            this.context = context;
            this.injectionFilter = injectionFilter;
        }

        @Override
        public String handleToken(String content) {
            Object parameter = context.get("_parameter");
            Object value = null;
            if (parameter == null) {
                context.put("value", null);
            } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                context.put("value", parameter);
                value = parameter;
            } else {
                // Get value out of paramMap
                value = OgnlCache.getValue(content, parameter);
            }
            if (Objects.isNull(value)) {
                // Get value out of binding
                value = OgnlCache.getValue(content, context);
            }
            if (Objects.isNull(value)) {
                throw new RuntimeException("Sql Parameter '" + content + "' Not Found");
            }
            String srtValue = Objects.toString(value, "");
            checkInjection(srtValue);
            return srtValue;
        }

        private void checkInjection(String value) {
            if (injectionFilter != null && !injectionFilter.matcher(value).matches()) {
                throw new ScriptingException("Invalid input. Please conform to regex" + injectionFilter.pattern());
            }
        }
    }

}