package com.gitee.sqlrest.template.fragment.impl;

import com.gitee.sqlrest.template.SqlContext;
import com.gitee.sqlrest.template.fragment.SqlFragment;
import java.util.List;
import java.util.Map;

public class ChooseFragment implements SqlFragment {

  private SqlFragment defaultSqlFragment;
  private List<SqlFragment> ifSqlFragments;

  public ChooseFragment(List<SqlFragment> ifSqlFragments,
      SqlFragment defaultSqlFragment) {
    this.ifSqlFragments = ifSqlFragments;
    this.defaultSqlFragment = defaultSqlFragment;
  }

  public boolean apply(SqlContext context) {
    for (SqlFragment sqlNode : ifSqlFragments) {
      if (sqlNode.apply(context)) {
        return true;
      }
    }
    if (defaultSqlFragment != null) {
      defaultSqlFragment.apply(context);
      return true;
    }
    return false;
  }

  public void applyParameter(Map<String, Boolean> names) {
    for (SqlFragment sqlNode : ifSqlFragments) {
      sqlNode.applyParameter(names);
    }
    if (defaultSqlFragment != null) {
      defaultSqlFragment.applyParameter(names);
    }
  }
}
