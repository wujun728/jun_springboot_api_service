package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlPara {
    String sql;
    List<Object> params;

    public SqlPara(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParmas() {
        return params;
    }

    @Override
    public String toString() {
        String result = sql;
        for (Object p : params) {
            if (p instanceof String) {
                result = Pattern.compile("?", Pattern.LITERAL).matcher(result)
                        .replaceFirst(Matcher.quoteReplacement(String.format("'%s'", p)));
                // result = result.replaceFirst("\\?", String.format("'%s'", p));
            } else {// TODO date and datetime
                result = Pattern.compile("?", Pattern.LITERAL).matcher(result)
                        .replaceFirst(Matcher.quoteReplacement(String.format("%s", p)));
                //result = result.replaceFirst("\\?", String.format("%s", p));
            }
        }
        return result;
    }
}
