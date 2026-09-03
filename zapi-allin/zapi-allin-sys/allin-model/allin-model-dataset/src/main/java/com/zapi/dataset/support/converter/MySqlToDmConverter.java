package com.zapi.dataset.support.converter;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.HashMap;

public class MySqlToDmConverter {

    private static final Map<String, String> TYPE_MAPPING = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("TEXT", "CLOB");
        put("LONGTEXT", "CLOB");
        put("VARCHAR", "VARCHAR2");
        put("DECIMAL", "NUMBER");
        put("DATETIME", "TIMESTAMP");
    }});

    private static final Map<String, String> FUNCTION_MAPPING = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("DATE_FORMAT\\$\\$([^,]+),\\s*'%Y-%m-%d %H:%i:%s'\\$\\$", "TO_CHAR($1, 'YYYY-MM-DD HH24:MI:SS')");
        put("STR_TO_DATE\\$\\$([^,]+),\\s*'%Y-%m-%d %H:%i:%s'\\$\\$", "TO_DATE($1, 'YYYY-MM-DD HH24:MI:SS')");
        put("NOW\\$\\$\\$\\$", "SYSDATE");
        put("IFNULL\\$\\$", "NVL(");
    }});

    public static String convert(String mysqlSql) {
        // 1. 统一换行符和空格处理
        String sql = normalizeSql(mysqlSql);

        // 2. 数据类型转换
        sql = convertDataTypes(sql);

        // 3. 函数转换
        sql = convertFunctions(sql);

        // 4. 分页查询转换
        sql = convertPagination(sql);

        // 5. 引号转换
        sql = sql.replace("`", "\"");

        return sql;
    }

    private static String normalizeSql(String sql) {
        return sql.replace("\r\n", "\n")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String convertDataTypes(String sql) {
        String pattern = "\\b(" + String.join("|", TYPE_MAPPING.keySet()) + ")\\b";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String type = m.group(1);
            m.appendReplacement(sb, TYPE_MAPPING.get(type.toUpperCase()));
        }
        m.appendTail(sb);

        // 特殊处理BIGINT → NUMBER(p,0)
        return sb.toString().replaceAll("(?i)BIGINT\\((\\d+)\\)", "NUMBER($1, 0)");
    }

    private static String convertFunctions(String sql) {
        for (Map.Entry<String, String> entry : FUNCTION_MAPPING.entrySet()) {
            sql = sql.replaceAll("(?i)" + entry.getKey(), entry.getValue());
        }
        return sql;
    }

    private static String convertPagination(String sql) {
        // 处理LIMIT分页
        sql = convertSimpleLimit(sql);
        sql = convertLimitOffset(sql);
        return sql;
    }

    private static String convertSimpleLimit(String sql) {
        // 处理 LIMIT n
        Pattern p = Pattern.compile("LIMIT\\s+(\\d+)\\s*;?$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = p.matcher(sql);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String replacement = "WHERE ROWNUM <= " + m.group(1);
            if (m.group().endsWith(";")) {
                replacement += ";";
            }
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private static String convertLimitOffset(String sql) {
        // 处理 LIMIT offset, count 或 LIMIT count OFFSET offset
        Pattern p1 = Pattern.compile("LIMIT\\s+(\\d+)\\s*,\\s*(\\d+)\\s*;?", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("LIMIT\\s+(\\d+)\\s+OFFSET\\s+(\\d+)\\s*;?", Pattern.CASE_INSENSITIVE);

        Matcher m1 = p1.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m1.find()) {
            int offset = Integer.parseInt(m1.group(1));
            int count = Integer.parseInt(m1.group(2));
            String replacement = String.format("WHERE ROWNUM BETWEEN %d AND %d",
                    offset + 1, offset + count);
            if (m1.group().endsWith(";")) {
                replacement += ";";
            }
            m1.appendReplacement(sb, replacement);
        }
        m1.appendTail(sb);

        Matcher m2 = p2.matcher(sb.toString());
        sb = new StringBuffer();
        while (m2.find()) {
            int count = Integer.parseInt(m2.group(1));
            int offset = Integer.parseInt(m2.group(2));
            String replacement = String.format("WHERE ROWNUM BETWEEN %d AND %d",
                    offset + 1, offset + count);
            if (m2.group().endsWith(";")) {
                replacement += ";";
            }
            m2.appendReplacement(sb, replacement);
        }
        m2.appendTail(sb);

        return sb.toString();
    }
}