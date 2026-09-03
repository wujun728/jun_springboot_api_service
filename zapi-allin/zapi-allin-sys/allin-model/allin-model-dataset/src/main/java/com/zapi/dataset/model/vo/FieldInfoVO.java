package com.zapi.dataset.model.vo;

import lombok.Data;

/**
 * 数据库表/视图字段信息
 */
@Data
public class FieldInfoVO {

    private Integer num; // 序号，更新时可以用到

    private String columnName;

    private String columnComment;

    private String columnType;

    private String oldName;
}
