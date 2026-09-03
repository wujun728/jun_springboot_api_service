package com.zy.utils;

import java.text.MessageFormat;

public class TskSqlUtils {

    public static String getInsert(String tableName, String cityCode, String cityName, String dpType, String name, String value) {
        String insertSql = "insert into {0}(SPTID,CITY_CODE,CITY_NAME,STATISTIC_DATE,DP_TYPE,NAME,VALUE,CREATE_DATE) values ({1},{2},{3},SYSDATE,{4},{5},{6},SYSDATE)";
        insertSql = MessageFormat.format(insertSql, tableName, "'" + System.currentTimeMillis() + "'", "'" + cityCode + "'", "'" + cityName + "'", "'" + dpType + "'", "'" + name + "'", "'" + value + "'");
        System.out.println(insertSql);
        return insertSql;
    }


    public static String getDelete(String tableName, String cityCode, String dpType, String name) {
        String deleteSql = "delete from {0} where DP_TYPE ={1} and NAME={2} and to_char(CREATE_DATE, {3}) = to_char(sysdate, {3}) and CITY_CODE = {4}";
        deleteSql = MessageFormat.format(deleteSql, tableName, "'" + dpType + "'", "'" + name + "'", "'yyyy-mm-dd'", "'" + cityCode + "'");
        System.out.println(deleteSql);
        return deleteSql;
    }

    public static String getClearOtherDay(String tableName) {
        String deleteSql = "delete from {0} where  to_char(CREATE_DATE, {1}) != to_char(sysdate, {1})";
        deleteSql = MessageFormat.format(deleteSql, tableName, "'yyyy-mm-dd'");
        System.out.println(deleteSql);
        return deleteSql;
    }

    public static void main(String[] args) {
        System.out.println(getDelete("aaa", "bbb", "ccc", "ddd"));
    }

}
