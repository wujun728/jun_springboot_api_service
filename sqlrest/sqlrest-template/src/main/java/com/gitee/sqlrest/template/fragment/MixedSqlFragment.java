package com.gitee.sqlrest.template.fragment;

import com.gitee.sqlrest.template.SqlContext;
import java.util.List;
import java.util.Map;

public class MixedSqlFragment implements SqlFragment {

  private List<SqlFragment> contents;

  public MixedSqlFragment(List<SqlFragment> contents) {
    this.contents = contents;
  }

  public boolean apply(SqlContext context) {
    for (SqlFragment sf : contents) {
      sf.apply(context);
    }

    return true;
  }

  public void applyParameter(Map<String, Boolean> names) {
    for(SqlFragment sf: contents) {
      sf.applyParameter(names);
    }

  }
}
