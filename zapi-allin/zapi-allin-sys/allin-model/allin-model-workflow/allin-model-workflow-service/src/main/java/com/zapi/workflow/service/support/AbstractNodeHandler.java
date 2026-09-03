package com.zapi.workflow.service.support;

import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象节点处理器基类
 * 提供节点处理器的通用实现和模板方法
 */
@Slf4j
public abstract class AbstractNodeHandler implements NodeHandler {
    
    @Override
    public void handle(NodeHandlerContext context, WfProcessInstance instance, 
                       ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        log.info("开始处理节点: {} [{}], 类型: {}", node.getName(), node.getId(), node.getType());
        
        try {
            // 前置处理
            beforeHandle(context, instance, definitionDTO, node);
            
            // 核心处理逻辑
            doHandle(context, instance, definitionDTO, node);
            
            // 后置处理
            afterHandle(context, instance, definitionDTO, node);
            
            log.info("节点处理完成: {} [{}]", node.getName(), node.getId());
        } catch (Exception e) {
            log.error("节点处理失败: {} [{}], 错误: {}", node.getName(), node.getId(), e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 前置处理（钩子方法，子类可覆盖）
     */
    protected void beforeHandle(NodeHandlerContext context, WfProcessInstance instance,
                                ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        // 默认空实现
    }
    
    /**
     * 核心处理逻辑（模板方法，子类必须实现）
     */
    protected abstract void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                                     ProcessDefinitionDTO definitionDTO, NodeDTO node);
    
    /**
     * 后置处理（钩子方法，子类可覆盖）
     */
    protected void afterHandle(NodeHandlerContext context, WfProcessInstance instance,
                               ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        // 默认空实现
    }
}
