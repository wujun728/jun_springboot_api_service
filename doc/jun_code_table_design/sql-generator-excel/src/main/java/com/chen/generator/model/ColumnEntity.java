package com.chen.generator.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 17:08
 * @description:
 * @version: 1.0
 */
@Data
@ExcelTarget("ColumnEntity")
public class ColumnEntity {

    @Excel(name = "列名")
    private String name;

    @Excel(name = "数据类型",orderNum = "1")
    private String type;

    @Excel(name = "键类型",orderNum = "2")
    private String key;

    @Excel(name = "是允许列否为空",orderNum = "3")
    private String isNullable;

    @Excel(name = "默认值",orderNum = "4")
    private String columnDefault;

    @Excel(name = "注释",orderNum = "5")
    private String comment;
}
