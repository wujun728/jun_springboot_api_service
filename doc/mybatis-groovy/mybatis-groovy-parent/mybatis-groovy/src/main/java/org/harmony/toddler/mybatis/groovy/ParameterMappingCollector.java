package org.harmony.toddler.mybatis.groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

public class ParameterMappingCollector {

    private final ParameterMapping[] parameterMappingSources;
    private final List<ParameterMapping> parameterMappings = new ArrayList<>();
    private final Map<String, Object> context;
    private final Configuration configuration;

    private int uid = 0;
    private String itemKey;

    public ParameterMappingCollector(ParameterMapping[] newParameterMappingSources, Map<String, Object> newContext,
                                     Configuration newConfiguration) {
        this.parameterMappingSources = newParameterMappingSources;
        this.context = newContext;
        this.configuration = newConfiguration;
    }

    public void setItemKey(String value) {
        this.itemKey = value;
    }

    public String getItemKey() {
        return this.itemKey;
    }

    public String g(int mapping) {
        ParameterMapping parameterMapping = this.parameterMappingSources[mapping];
        PropertyInfo vi = getPropertyInfo(parameterMapping.getProperty());
        if (vi.isIterable) {
            parameterMapping = itemize(parameterMapping, vi);
            this.context.put(vi.root, this.context.get(this.itemKey));
        }
        this.parameterMappings.add(parameterMapping);
        return "?";
    }

    public List<ParameterMapping> getParameterMappings() {
        return this.parameterMappings;
    }

    private ParameterMapping itemize(ParameterMapping source, PropertyInfo var) {
        StringBuilder sb = new StringBuilder().append("_RPTITEM_").append(this.uid++);
        var.root = sb.toString();
        String propertyName = sb.append(var.path).toString();
        ParameterMapping.Builder builder = new ParameterMapping.Builder(this.configuration, propertyName,
                source.getJavaType());
        builder.expression(source.getExpression()).jdbcType(source.getJdbcType()).jdbcTypeName(source.getJdbcTypeName())
                .mode(source.getMode()).numericScale(source.getNumericScale()).resultMapId(source.getResultMapId())
                .typeHandler(source.getTypeHandler());
        return builder.build();
    }

    private PropertyInfo getPropertyInfo(String name) {
        PropertyInfo i = new PropertyInfo();
        if (name != null) {
            int p = name.indexOf('.');
            if (p == -1) {
                i.root = name;
            } else {
                i.root = name.substring(0, p);
                i.path = name.substring(p);
            }
        }
        i.isIterable = this.itemKey != null && this.itemKey.equals(i.root);
        return i;
    }

    static class PropertyInfo {
        boolean isIterable = false;
        String root = "";
        String path = "";

        public PropertyInfo() {
            // Prevent synthetic access
        }
    }

}
