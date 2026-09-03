package com.zapi.workflow.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程测试订单实体类
 * 对应数据库表：zapi_flow_test_order
 * 用于工作流测试的简单订单数据
 */
@Data
@TableName("zapi_flow_test_order")
public class FlowTestOrder {
    
    /**
     * 订单ID
     * 主键，自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     * 订单的唯一标识，例如：ORD20241226001
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 订单名称
     * 订单的显示名称，例如：测试订单001
     */
    @TableField("order_name")
    private String orderName;
    
    /**
     * 订单数量
     * 订单包含的商品数量
     */
    @TableField("order_count")
    private Integer orderCount;
}
