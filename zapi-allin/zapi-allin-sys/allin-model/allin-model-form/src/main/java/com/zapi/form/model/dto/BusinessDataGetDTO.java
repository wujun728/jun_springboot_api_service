package com.zapi.form.model.dto;

import lombok.Data;

/**
 * 业务数据主键查询请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
//业务数据主键查询请求参数")
public class BusinessDataGetDTO {

    ///表单ID
    private String formId;

    ///业务数据主键ID
    private Long id;
} 