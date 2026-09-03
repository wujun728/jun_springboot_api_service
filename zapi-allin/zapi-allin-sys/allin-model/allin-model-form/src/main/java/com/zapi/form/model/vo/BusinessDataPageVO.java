package com.zapi.form.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 业务数据分页查询返回结果
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
@Data

public class BusinessDataPageVO {

   /// 业务数据记录列表
    private List<Map<String, Object>> records;

    ///总记录数
    private Long total;

    ///表格列配置（列名集合或列配置集合）
    private List<?> columns;

    ///当前页码
    private Long pageNo;

    ///每页数量
    private Long pageSize;
} 