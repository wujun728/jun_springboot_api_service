package com.zapi.form.model.dto;

import lombok.Data;

/**
 * 业务数据删除请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
public class BusinessDataDeleteDTO {

    ///表单ID
    private String formId;

    ///业务数据主键ID
    private Long id;
} 