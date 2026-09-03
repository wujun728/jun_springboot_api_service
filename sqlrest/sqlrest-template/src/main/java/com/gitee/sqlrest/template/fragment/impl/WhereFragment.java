package com.gitee.sqlrest.template.fragment.impl;

import com.gitee.sqlrest.template.fragment.SqlFragment;
import java.util.Arrays;
import java.util.List;

public class WhereFragment extends TrimFragment {

  private static List<String> prefixList = Arrays
      .asList("AND ", "OR ", "AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");

  public WhereFragment(SqlFragment contents) {
    super(contents, "WHERE", null, prefixList, null);
  }

}
