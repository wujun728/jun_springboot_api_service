package com.chen.generator.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 17:08
 * @description:
 * @version: 1.0
 */
@Data
public class TableEntity {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表的注释
     */
    private String tableComment;

    /**
     * 表下面的列
     */
    private List<ColumnEntity> columns;
}
