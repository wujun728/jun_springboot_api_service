package com.zapi.dataset.support.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DmToMySqlConverter {

    private static final Map<String, String> TYPE_MAPPING;
    private static final Map<String, String> FUNCTION_MAPPING;

    static {
        // 初始化 TYPE_MAPPING
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("CLOB", "TEXT");
        typeMap.put("VARCHAR2", "VARCHAR");
        typeMap.put("NUMBER", "DECIMAL");
        typeMap.put("BLOB", "LONGBLOB");
        TYPE_MAPPING = Collections.unmodifiableMap(typeMap);

        // 初始化 FUNCTION_MAPPING
        Map<String, String> functionMap = new HashMap<>();
        functionMap.put("TO_CHAR\\(([^,]+),\\s*'YYYY-MM-DD HH24:MI:SS'\\)", "DATE_FORMAT($1, '%Y-%m-%d %H:%i:%s')");
        functionMap.put("TO_DATE\\(([^,]+),\\s*'YYYY-MM-DD HH24:MI:SS'\\)", "STR_TO_DATE($1, '%Y-%m-%d %H:%i:%s')");
        functionMap.put("SYSDATE", "NOW()");
        functionMap.put("NVL\\(", "IFNULL(");
        FUNCTION_MAPPING = Collections.unmodifiableMap(functionMap);
    }

    public static String convert(String dmSql) {
        // 1. 统一换行符和空格处理
        String sql = normalizeSql(dmSql);

        // 2. 数据类型转换
        sql = convertDataTypes(sql);

        // 3. 函数转换
        sql = convertFunctions(sql);

        // 4. 分页查询转换
        sql = convertPagination(sql);

        // 5. 引号转换
        sql = sql.replace("\"", "`");

        // 6. 序列处理
        sql = handleSequences(sql);

        // 7. 表名转换为小写
        sql = convertTableNamesToLowercase(sql);

        // 8. 字段名转换为小写
        sql = convertColumnNamesToLowercase(sql);

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

        // 特殊处理NUMBER(p,0) → BIGINT
        return sb.toString().replaceAll("NUMBER\\(\\s*(\\d+)\\s*,\\s*0\\s*\\)", "BIGINT($1)");
    }

    private static String convertFunctions(String sql) {
        for (Map.Entry<String, String> entry : FUNCTION_MAPPING.entrySet()) {
            sql = sql.replaceAll("(?i)" + entry.getKey(), entry.getValue());
        }
        return sql;
    }

    private static String convertPagination(String sql) {
        // 处理ROWNUM分页
        sql = sql.replaceAll("(?i)WHERE\\s+ROWNUM\\s*<=\\s*(\\d+)", "LIMIT $1");
        sql = sql.replaceAll("(?i)ROWNUM\\s*<=\\s*(\\d+)", "LIMIT $1");
        return sql;
    }

    private static String handleSequences(String sql) {
        // 移除序列创建语句
        sql = sql.replaceAll("(?i)CREATE SEQUENCE\\s+\\w+\\s+.*?;", "");
        // 简单处理序列引用
        return sql.replaceAll("(?i)\\.NEXTVAL", "");
    }

    private static String convertTableNamesToLowercase(String sql) {
        // 匹配常见的 SQL 关键字后面的表名
        String pattern = "\\b(FROM|JOIN|UPDATE|INTO)\\s+(\\w+)";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String keyword = m.group(1);
            String tableName = m.group(2);
            m.appendReplacement(sb, keyword + " " + tableName.toLowerCase());
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private static String convertColumnNamesToLowercase(String sql) {
        // 匹配 SELECT 语句中的字段名
        String selectPattern = "\\bSELECT\\s+([^FROM]+)FROM";
        Pattern selectP = Pattern.compile(selectPattern, Pattern.CASE_INSENSITIVE);
        Matcher selectM = selectP.matcher(sql);

        StringBuffer sb = new StringBuffer();
        while (selectM.find()) {
            String columns = selectM.group(1);
            // 分割字段名
            String[] columnArray = columns.split(",");
            StringBuilder newColumns = new StringBuilder();
            for (String column : columnArray) {
                // 去除前后空格
                column = column.trim();
                // 查找字段名（忽略别名）
                int asIndex = column.toUpperCase().indexOf(" AS ");
                String columnName;
                if (asIndex != -1) {
                    columnName = column.substring(0, asIndex).trim();
                    String alias = column.substring(asIndex + 4).trim();
                    newColumns.append(columnName.toLowerCase()).append(" AS ").append(alias).append(",");
                } else {
                    newColumns.append(column.toLowerCase()).append(",");
                }
            }
            if (newColumns.length() > 0) {
                newColumns.deleteCharAt(newColumns.length() - 1); // 移除最后一个逗号
            }
            selectM.appendReplacement(sb, "SELECT " + newColumns + " FROM");
        }
        selectM.appendTail(sb);

        // 处理 INSERT 语句中的字段名
        String insertPattern = "\\bINSERT\\s+INTO\\s+\\w+\\s*\\(([^)]+)\\)";
        Pattern insertP = Pattern.compile(insertPattern, Pattern.CASE_INSENSITIVE);
        Matcher insertM = insertP.matcher(sb.toString());

        StringBuffer insertSb = new StringBuffer();
        while (insertM.find()) {
            String columns = insertM.group(1);
            String[] columnArray = columns.split(",");
            StringBuilder newColumns = new StringBuilder();
            for (String column : columnArray) {
                newColumns.append(column.trim().toLowerCase()).append(",");
            }
            if (newColumns.length() > 0) {
                newColumns.deleteCharAt(newColumns.length() - 1); // 移除最后一个逗号
            }
            insertM.appendReplacement(insertSb, "INSERT INTO " + insertM.group().substring(insertM.group().indexOf("(")) + newColumns + ")");
        }
        insertM.appendTail(insertSb);

        return insertSb.toString();
    }
}