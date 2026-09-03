package com.zapi.form.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 业务数据批量删除请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
public class BusinessDataBatchDeleteDTO {

   ///表单ID
    private String formId;

    ///业务数据主键ID列表
    private List<Long> ids;
} 