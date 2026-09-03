package com.zapi.workflow.service.support;

import com.zapi.workflow.service.mapper.WfProcessInstanceMapper;
import com.zapi.workflow.service.mapper.WfTaskMapper;
import com.zapi.workflow.service.model.dto.FlowDTO;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.service.WfHistoryActivityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 节点处理器上下文
 * 封装节点处理过程中所需的公共依赖和工具方法
 */
@Component
@RequiredArgsConstructor
@Getter
public class NodeHandlerContext {
    
    private final WfTaskMapper taskMapper;
    private final WfProcessInstanceMapper processInstanceMapper;
    private final WfHistoryActivityService historyActivityService;
    private final AssigneeResolver assigneeResolver;
    
    /**
     * 处理下一个节点
     * 由 NodeHandlerRegistry 调用，实现节点处理的链式调用
     */
    private NodeHandlerRegistry nodeHandlerRegistry;
    
    /**
     * 设置节点处理器注册表（避免循环依赖）
     */
    public void setNodeHandlerRegistry(NodeHandlerRegistry nodeHandlerRegistry) {
        this.nodeHandlerRegistry = nodeHandlerRegistry;
    }
    
    /**
     * 处理下一个节点
     * @param instance 流程实例
     * @param definitionDTO 流程定义
     * @param nextNodeId 下一个节点ID
     */
    public void processNextNode(WfProcessInstance instance, ProcessDefinitionDTO definitionDTO, String nextNodeId) {
        // 找到下一个节点
        NodeDTO nextNode = definitionDTO.getNodes().stream()
                .filter(node -> nextNodeId.equals(node.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("节点不存在: " + nextNodeId));
        
        // 委托给注册表处理
        nodeHandlerRegistry.handleNode(instance, definitionDTO, nextNode);
    }
    
    /**
     * 解析审批人
     * 委托给 AssigneeResolver 处理
     */
    public String resolveAssignee(NodeDTO node, WfProcessInstance instance) {
        return assigneeResolver.resolve(node, instance);
    }
    
    /**
     * 获取节点的所有流出连线
     */
    public List<FlowDTO> findOutgoingFlows(ProcessDefinitionDTO definitionDTO, String nodeId) {
        return definitionDTO.getFlows().stream()
                .filter(flow -> nodeId.equals(flow.getFrom()))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取节点的第一条流出连线目标节点
     */
    public String findSingleNextNode(ProcessDefinitionDTO definitionDTO, String nodeId) {
        List<FlowDTO> outgoing = findOutgoingFlows(definitionDTO, nodeId);
        if (outgoing.isEmpty()) {
            return null;
        }
        return outgoing.get(0).getTo();
    }
    
    /**
     * 解析流程变量
     * 委托给 AssigneeResolver 处理
     */
    public Map<String, Object> parseVariables(WfProcessInstance instance) {
        return assigneeResolver.parseVariables(instance);
    }
    
    /**
     * 更新流程实例当前节点并保存
     */
    public void updateCurrentNode(WfProcessInstance instance, String nodeId) {
        instance.setCurrentNodeId(nodeId);
        processInstanceMapper.updateById(instance);
    }
}
