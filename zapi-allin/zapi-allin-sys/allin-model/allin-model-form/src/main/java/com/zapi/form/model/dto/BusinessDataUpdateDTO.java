package com.zapi.form.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * 业务数据修改请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
public class BusinessDataUpdateDTO {

   //表单ID
    private String formId;

    //业务数据主键ID
    private Long id;

   //业务数据字段值
    private Map<String, Object> data;
} 