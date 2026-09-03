package com.gitee.sqlrest.template.fragment.impl;

import com.gitee.sqlrest.template.SqlContext;
import com.gitee.sqlrest.template.fragment.SqlFragment;
import com.gitee.sqlrest.template.token.GenericTokenParser;
import com.gitee.sqlrest.template.token.TokenHandler;
import com.gitee.sqlrest.template.util.OgnlCacheUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TextFragment implements SqlFragment {

  private String sql;

  public TextFragment(String sql) {
    this.sql = sql;
  }

  public boolean apply(final SqlContext context) {
    GenericTokenParser parser2 = new GenericTokenParser("${", "}",
        new TokenHandler() {

          public String handleToken(String content) {
            Object value = OgnlCacheUtils.getValue(content,
                context.getBinding());
            return value == null ? "" : value.toString();
          }
        });

    context.appendSql(parser2.parse(sql));
    return true;
  }

  public void applyParameter(Map<String, Boolean> names) {
    List<String> openTokens = Arrays.asList("${", "#{");
    String closeToken = "}";
    for (String openToken : openTokens) {
      new GenericTokenParser(openToken, closeToken,
          (name) -> {
            names.putIfAbsent(name, false);
            return name;
          })
          .parse(sql);
    }
  }
}
