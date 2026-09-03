package com.gitee.sqlrest.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

public class SqlContext {

  public static final String BINDING_DATA = "_data";

  static {
    OgnlRuntime.setPropertyAccessor(HashMap.class, new ContextAccessor());
  }

  private Configuration cfg;
  private Map<String, Object> binding;
  private StringBuilder sql = new StringBuilder();
  private List<Object> parameter;
  private int uniqueNumber = 0;

  public SqlContext(Configuration cfg, Object data) {
    this.cfg = cfg;
    binding = new HashMap<>();
    parameter = new ArrayList<>();
    binding.put(BINDING_DATA, data);
  }

  public void bind(String key, Object value) {
    binding.put(key, value);
  }

  public void appendSql(String sqlFragment) {
    sql.append(sqlFragment).append(" ");
  }

  public Map<String, Object> getBinding() {
    return this.binding;
  }

  public List<Object> getParameter() {
    return this.parameter;
  }

  public void addParameter(Object parameter) {
    this.parameter.add(parameter);
  }

  public String getSql() {
    return sql.toString();
  }

  public void setSql(String sql) {
    this.sql = new StringBuilder(sql);
  }

  public int getUniqueNumber() {
    return ++uniqueNumber;
  }

  public Configuration getConfiguration() {
    return this.cfg;
  }

  static class ContextAccessor implements PropertyAccessor {

    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
      Map map = (Map) target;
      Object result = map.get(name);
      if (result != null) {
        return result;
      }

      Object parameterObject = map.get(BINDING_DATA);
      if (parameterObject instanceof Map) {
        return ((Map) parameterObject).get(name);
      }

      return null;
    }

    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
      Map map = (Map) target;
      map.put(name, value);
    }

    public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
      return null;
    }

    public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
      return null;
    }

  }

}
