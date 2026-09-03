package com.zapi.form.model.dto;
import lombok.Data;

import java.util.Map;

/**
 * 业务数据分页查询请求参数
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data
public class BusinessDataPageDTO{

    //表单ID
    private String formId;
    private Map<String, Object> criteria;
    private int pageNo;
    private int pageSize;
} 