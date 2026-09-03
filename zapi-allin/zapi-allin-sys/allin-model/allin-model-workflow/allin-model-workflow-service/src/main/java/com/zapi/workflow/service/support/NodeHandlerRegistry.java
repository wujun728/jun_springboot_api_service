package com.zapi.workflow.service.support;

import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节点处理器注册表
 * 负责管理所有节点处理器，根据节点类型分发到对应的处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NodeHandlerRegistry {
    
    private final List<NodeHandler> nodeHandlers;
    private final NodeHandlerContext nodeHandlerContext;
    
    private final Map<String, NodeHandler> handlerMap = new HashMap<>();
    
    /**
     * 初始化时注册所有处理器
     */
    @PostConstruct
    public void init() {
        // 注册所有节点处理器
        for (NodeHandler handler : nodeHandlers) {
            String nodeType = handler.getNodeType();
            NodeHandler existing = handlerMap.get(nodeType);
            
            // 如果已存在处理器，根据优先级决定是否替换
            if (existing != null) {
                if (handler.getOrder() < existing.getOrder()) {
                    handlerMap.put(nodeType, handler);
                    log.info("节点处理器已替换: {} -> {}", nodeType, handler.getClass().getSimpleName());
                }
            } else {
                handlerMap.put(nodeType, handler);
                log.info("节点处理器已注册: {} -> {}", nodeType, handler.getClass().getSimpleName());
            }
        }
        
        // 设置上下文中的注册表引用（避免循环依赖）
        nodeHandlerContext.setNodeHandlerRegistry(this);
        
        log.info("节点处理器注册完成，共注册 {} 个处理器", handlerMap.size());
    }
    
    /**
     * 根据节点类型获取对应的处理器
     * @param nodeType 节点类型
     * @return 节点处理器
     */
    public NodeHandler getHandler(String nodeType) {
        NodeHandler handler = handlerMap.get(nodeType);
        if (handler == null) {
            throw new RuntimeException("不支持的节点类型: " + nodeType);
        }
        return handler;
    }
    
    /**
     * 处理节点
     * @param instance 流程实例
     * @param definitionDTO 流程定义
     * @param node 当前节点
     */
    public void handleNode(WfProcessInstance instance, ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        NodeHandler handler = getHandler(node.getType());
        handler.handle(nodeHandlerContext, instance, definitionDTO, node);
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
        
        handleNode(instance, definitionDTO, nextNode);
    }
    
    /**
     * 检查是否有处理器支持指定节点类型
     */
    public boolean hasHandler(String nodeType) {
        return handlerMap.containsKey(nodeType);
    }
}
