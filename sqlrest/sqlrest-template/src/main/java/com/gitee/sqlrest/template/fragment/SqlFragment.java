package com.gitee.sqlrest.template.fragment;

import com.gitee.sqlrest.template.SqlContext;
import java.util.Map;

public interface SqlFragment {

  boolean apply(SqlContext context);

  void applyParameter(Map<String, Boolean> names);
}
