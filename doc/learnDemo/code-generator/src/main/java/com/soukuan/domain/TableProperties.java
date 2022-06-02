package com.soukuan.domain;

import com.soukuan.support.GeneratorSupport;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TableProperties implements Serializable {

    private static final Map<String, String> MYSQL_TYPE_COLUMN_TO_FIELD = new HashMap<String, String>() {
        {
            put("BIGINT", "Long");
            put("VARCHAR", "String");
            put("INT", "Integer");
            put("DATETIME", "java.util.Date");
            put("TEXT", "String");
            put("DECIMAL", "java.math.BigDecimal");

        }
    };

    private static final Map<String, String> MYSQL_TYPE_COLUMN_TO_JDBC = new HashMap<String, String>() {
        {
            put("BIGINT", "BIGINT");
            put("VARCHAR", "VARCHAR");
            put("INT", "INTEGER");
            put("DATETIME", "DATE");
            put("TEXT", "LONGVARCHAR");
            put("DECIMAL", "DECIMAL");
        }
    };

    /**
     * 首字母大写驼峰
     */
    private String modelNameUpperCamel;

    /**
     * 首字母小写驼峰
     */
    private String modelNameLowerCamel;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 作者
     */
    private String author;

    /**
     * 表字段名称集合
     */
    private List<String> columnNameList;
    /**
     * 表字段类型
     */
    private List<String> columnTypeList;
    /**
     * 字段备注
     */
    private List<String> columnComments;
    /**
     * java属性名称
     */
    private List<String> fieldNameList;
    /**
     * java属性类型
     */
    private List<String> fieldTypeList;
    /**
     * jdbc属性类型
     */
    private List<String> jdbcTypeList;

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
        //设置java字段名称
        List<String> fieldNameList = new ArrayList<>();
        columnNameList.forEach(column -> {
            fieldNameList.add(GeneratorSupport.convertLowerCamel(column));
        });
        setFieldNameList(fieldNameList);
    }

    public void setColumnTypeList(List<String> columnTypeList) {
        this.columnTypeList = columnTypeList;
        //设置java字段类型
        List<String> fieldTypeList = new ArrayList<>();
        List<String> jdbcTypeList = new ArrayList<>();
        columnTypeList.forEach(column -> {
            fieldTypeList.add(MYSQL_TYPE_COLUMN_TO_FIELD.get(column));
            jdbcTypeList.add(MYSQL_TYPE_COLUMN_TO_JDBC.get(column));
        });
        setFieldTypeList(fieldTypeList);
        setJdbcTypeList(jdbcTypeList);
    }
}
