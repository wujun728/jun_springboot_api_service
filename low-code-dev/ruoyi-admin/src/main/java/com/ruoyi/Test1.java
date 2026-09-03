package com.ruoyi;

import static com.ruoyi.common.utils.sql.SqlUtil.toSqlToJava;

/**
 * @author 李文
 * @create 2023-03-16 10:50
 **/
public class Test1
{

    public static void main(String[] args) throws Exception {

        String sqlType = "blob";
        System.out.println(toSqlToJava(sqlType));

    }
}
