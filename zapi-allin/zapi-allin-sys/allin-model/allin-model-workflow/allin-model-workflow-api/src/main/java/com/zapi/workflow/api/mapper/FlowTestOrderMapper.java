package com.zapi.workflow.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workflow.api.model.entity.FlowTestOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 流程测试订单数据访问层接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 额外提供订单特有的查询方法
 */
@Mapper
public interface FlowTestOrderMapper extends BaseMapper<FlowTestOrder> {
    
    /**
     * 根据订单号查询订单
     * 订单号应该是唯一的
     *
     * @param orderNo 订单号，例如：ORD20241226001
     * @return 对应的订单实体，如果不存在则返回null
     */
    @Select("select * from zapi_flow_test_order where order_no = #{orderNo}")
    FlowTestOrder selectByOrderNo(String orderNo);
    
    /**
     * 统计指定名称的订单数量
     * 用于检查重名订单
     *
     * @param orderName 订单名称
     * @return 该名称的订单数量
     */
    @Select("select count(*) from zapi_flow_test_order where order_name = #{orderName}")
    Integer countByOrderName(String orderName);
}
