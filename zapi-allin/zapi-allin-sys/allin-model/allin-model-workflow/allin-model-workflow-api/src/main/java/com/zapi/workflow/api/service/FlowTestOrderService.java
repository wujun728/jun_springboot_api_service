package com.zapi.workflow.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.api.model.entity.FlowTestOrder;

/**
 * 流程测试订单服务接口
 * 提供订单的业务逻辑处理，包括CRUD操作和业务验证
 */
public interface FlowTestOrderService extends IService<FlowTestOrder> {
    
    /**
     * 根据订单号查询订单
     * 订单号应该是唯一的
     *
     * @param orderNo 订单号，例如：ORD20241226001
     * @return 对应的订单实体，如果不存在则返回null
     */
    FlowTestOrder getByOrderNo(String orderNo);
    
    /**
     * 创建新订单
     * 会进行业务验证，如订单号唯一性检查等
     *
     * @param order 订单信息
     * @return 创建成功的订单实体（包含生成的ID）
     * @throws RuntimeException 当订单号已存在或其他验证失败时抛出
     */
    FlowTestOrder createOrder(FlowTestOrder order);
    
    /**
     * 更新订单信息
     * 会进行业务验证，如订单号唯一性检查等
     *
     * @param order 更新的订单信息（必须包含ID）
     * @return 更新后的订单实体
     * @throws RuntimeException 当订单不存在或验证失败时抛出
     */
    FlowTestOrder updateOrder(FlowTestOrder order);
    
    /**
     * 分页查询订单列表
     * 支持按订单名称模糊查询
     *
     * @param page 分页参数
     * @param orderName 订单名称（可选，用于模糊查询）
     * @return 分页结果
     */
    IPage<FlowTestOrder> pageQuery(Page<FlowTestOrder> page, String orderName);
    
    /**
     * 检查订单号是否已存在
     *
     * @param orderNo 订单号
     * @param excludeId 排除的订单ID（用于更新时检查，可为null）
     * @return true表示已存在，false表示不存在
     */
    boolean existsByOrderNo(String orderNo, Long excludeId);
}
