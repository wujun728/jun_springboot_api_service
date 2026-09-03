package com.gitee.sqlrest.template.fragment.impl;

import com.gitee.sqlrest.template.SqlContext;
import com.gitee.sqlrest.template.fragment.ExpressionEvaluator;
import com.gitee.sqlrest.template.fragment.SqlFragment;
import java.util.Map;

public class IfFragment implements SqlFragment {

  private String test;
  private SqlFragment contents;
  private ExpressionEvaluator expression;

  public IfFragment(SqlFragment contents, String test) {
    this.expression = new ExpressionEvaluator();
    this.contents = contents;
    this.test = test;
  }

  public boolean apply(SqlContext context) {
    if (expression.evaluateBoolean(test, context.getBinding())) {
      this.contents.apply(context);
      return true;
    }
    return false;
  }

  public void applyParameter(Map<String, Boolean> names) {
    this.contents.applyParameter(names);
  }

}
