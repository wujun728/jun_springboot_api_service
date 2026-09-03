package com.zapi.workflow.service.support;

import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;

/**
 * 节点处理器接口
 * 策略模式的核心接口，定义节点处理的统一规范
 */
public interface NodeHandler {
    
    /**
     * 获取当前处理器支持的节点类型
     * @return 节点类型字符串，对应 ProcessConstants.NodeType 中的常量
     */
    String getNodeType();
    
    /**
     * 处理节点
     * @param context 节点处理上下文，包含处理节点所需的所有依赖和数据
     * @param instance 流程实例
     * @param definitionDTO 流程定义
     * @param node 当前处理的节点
     */
    void handle(NodeHandlerContext context, WfProcessInstance instance, 
                ProcessDefinitionDTO definitionDTO, NodeDTO node);
    
    /**
     * 获取处理器优先级
     * 数值越小优先级越高，用于同类型处理器的选择
     * @return 优先级数值，默认为0
     */
    default int getOrder() {
        return 0;
    }
}
