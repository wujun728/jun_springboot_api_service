package com.zapi.dataset.model.req;

import lombok.Data;

@Data
public class SqlConvertRequest {
    private String sourceDbType;  // 源数据库类型
    private String targetDbType; // 目标数据库类型
    private String sqlContent;   // 要转换的SQL内容
}