package com.zapi.form.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * 业务数据新增请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
public class BusinessDataCreateDTO {

   ///表单ID
    private String formId;

   ///业务数据字段值
    private Map<String, Object> data;
    
   ///流程发起人ID（前端传递，用于发起流程时设置startUser）
    private String startUser;
    
   ///应用ID（可选，用于判断是否需要发起流程）
    private String appId;
    
   ///目录ID（可选，用于查询目录详情判断是否为流程表单）
    private String entryId;
    
   ///是否为流程表单（前端传递，0=普通表单，1=流程表单）
    private Integer hasFlow;
} 