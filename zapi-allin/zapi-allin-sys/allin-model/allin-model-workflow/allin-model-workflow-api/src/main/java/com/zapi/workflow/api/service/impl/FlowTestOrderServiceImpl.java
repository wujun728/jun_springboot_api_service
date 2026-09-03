package com.zapi.workflow.api.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.workflow.api.mapper.FlowTestOrderMapper;
import com.zapi.workflow.api.model.entity.FlowTestOrder;
import com.zapi.workflow.api.service.FlowTestOrderService;
import com.zapi.workflow.service.model.dto.StartProcessDTO;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import com.zapi.workflow.service.service.WfProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程测试订单服务实现类
 * 实现订单的业务逻辑处理
 */
@Service
@Slf4j
public class FlowTestOrderServiceImpl extends ServiceImpl<FlowTestOrderMapper, FlowTestOrder> implements FlowTestOrderService {


    /**
     * 流程实例服务
     * 负责流程运行时的管理
     */
    @Autowired
    private WfProcessInstanceService processInstanceService;

    @Autowired
    private WfProcessDefinitionService wfProcessDefinitionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public FlowTestOrder getByOrderNo(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            return null;
        }
        return baseMapper.selectByOrderNo(orderNo);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlowTestOrder createOrder(FlowTestOrder order) {
        // 验证必填字段
        if (order == null) {
            throw new RuntimeException("订单信息不能为空");
        }
        if (!StringUtils.hasText(order.getOrderNo())) {
            throw new RuntimeException("订单号不能为空");
        }
        if (!StringUtils.hasText(order.getOrderName())) {
            throw new RuntimeException("订单名称不能为空");
        }
        if (order.getOrderCount() == null || order.getOrderCount() <= 0) {
            throw new RuntimeException("订单数量必须大于0");
        }
        
        // 检查订单号唯一性
        if (existsByOrderNo(order.getOrderNo(), null)) {
            throw new RuntimeException("订单号已存在：" + order.getOrderNo());
        }
        
        // 保存订单
        boolean success = save(order);
        if (!success) {
            throw new RuntimeException("创建订单失败");
        }

        log.info("创建订单数据添加完成，主键标识：" + order.getOrderNo());

        log.info("开始发起工作流");
        /***
         * 发起工作流
         */
        StartProcessDTO startProcessDTO=new StartProcessDTO();
        /***
         * 查询wf_process_definition流程定义表中
         * business_type 业务类型：1-系统表单 2-业务表单
         * business_type === 1
         * 系统表单标识（系统表单类型时使用）
         * system_form_key === “zapi_flow_test_order”
         *
         * id就是  fadd85b65bc31cd01fe0352442176d63
         */
        WfProcessDefinition info =wfProcessDefinitionService.getByBusinessTypeAndForm(1,"zapi_flow_test_order",null,1);

        if (ObjectUtil.isEmpty(info)) {
            return order;
        }
        
        // 获取当前登录用户ID（使用公共工具类）
        String currentUserId = com.zapi.util.UserContext.getCurrentUserIdStr();
        if (currentUserId == null) {
            log.error("发起工作流失败：无法获取当前登录用户ID");
            throw new RuntimeException("发起工作流失败：请先登录");
        }
        
        startProcessDTO.setProcessDefinitionId(info.getId());
        /// 业务数据ID
        startProcessDTO.setBusinessKey(order.getId().toString());
        /// 启动流程用户ID，取当前用户ID
        startProcessDTO.setStartUser(currentUserId);
        startProcessDTO.setVariables(convertToMap(order));
        log.info("发起工作流请求参数："+ JSONObject.toJSONString(startProcessDTO));
        WfProcessInstance result = processInstanceService.startProcess(startProcessDTO);
        log.info("发起工作流结束："+ JSONObject.toJSONString(result));
        return order;
    }

    public Map<String, Object> convertToMap(FlowTestOrder order) {
        if (order == null) {
            return new HashMap<>();
        }

        try {
            // 使用Jackson将对象转换为Map
            return objectMapper.convertValue(order, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("转换FlowTestOrder到Map失败", e);
        }
    }
    
    @Override
    public FlowTestOrder updateOrder(FlowTestOrder order) {
        // 验证必填字段
        if (order == null || order.getId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (!StringUtils.hasText(order.getOrderNo())) {
            throw new RuntimeException("订单号不能为空");
        }
        if (!StringUtils.hasText(order.getOrderName())) {
            throw new RuntimeException("订单名称不能为空");
        }
        if (order.getOrderCount() == null || order.getOrderCount() <= 0) {
            throw new RuntimeException("订单数量必须大于0");
        }
        
        // 检查订单是否存在
        FlowTestOrder existingOrder = getById(order.getId());
        if (existingOrder == null) {
            throw new RuntimeException("订单不存在，ID：" + order.getId());
        }
        
        // 检查订单号唯一性（排除当前订单）
        if (existsByOrderNo(order.getOrderNo(), order.getId())) {
            throw new RuntimeException("订单号已存在：" + order.getOrderNo());
        }
        
        // 更新订单
        boolean success = updateById(order);
        if (!success) {
            throw new RuntimeException("更新订单失败");
        }
        
        return order;
    }
    
    @Override
    public IPage<FlowTestOrder> pageQuery(Page<FlowTestOrder> page, String orderName) {
        LambdaQueryWrapper<FlowTestOrder> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果提供了订单名称，则进行模糊查询
        if (StringUtils.hasText(orderName)) {
            queryWrapper.like(FlowTestOrder::getOrderName, orderName);
        }
        
        // 按ID降序排列（最新的在前面）
        queryWrapper.orderByDesc(FlowTestOrder::getId);
        
        return page(page, queryWrapper);
    }
    
    @Override
    public boolean existsByOrderNo(String orderNo, Long excludeId) {
        if (!StringUtils.hasText(orderNo)) {
            return false;
        }
        
        LambdaQueryWrapper<FlowTestOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowTestOrder::getOrderNo, orderNo);
        
        // 如果提供了排除ID，则排除该记录
        if (excludeId != null) {
            queryWrapper.ne(FlowTestOrder::getId, excludeId);
        }
        
        return count(queryWrapper) > 0;
    }
}
