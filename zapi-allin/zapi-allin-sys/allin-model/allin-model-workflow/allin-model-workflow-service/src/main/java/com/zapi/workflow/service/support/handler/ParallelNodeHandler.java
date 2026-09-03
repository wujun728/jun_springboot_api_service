package com.zapi.workflow.service.support.handler;

import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.model.dto.FlowDTO;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.support.AbstractNodeHandler;
import com.zapi.workflow.service.support.NodeHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 并行节点处理器
 * 将流程同时路由到多个分支并行执行
 */
@Slf4j
@Component
public class ParallelNodeHandler extends AbstractNodeHandler {
    
    @Override
    public String getNodeType() {
        return ProcessConstants.NodeType.PARALLEL;
    }
    
    @Override
    protected void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                            ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        String strategy = StringUtils.hasText(node.getStrategy()) ? node.getStrategy() : "ALL";
        
        // 记录并行节点完成
        context.getHistoryActivityService().recordCompleteActivity(
            instance.getId(),
            node.getId(),
            node.getName(),
            node.getType(),
            instance.getStartUser(),
            "并行分支，执行策略: " + strategy
        );
        
        // 获取所有流出连线
        List<FlowDTO> outgoingFlows = context.findOutgoingFlows(definitionDTO, node.getId());
        if (outgoingFlows.isEmpty()) {
            log.warn("并行节点没有流出连线，流程结束: 节点={}", node.getName());
            // 没有流出连线，直接结束
            completeProcess(context, instance);
            return;
        }
        
        // 更新当前节点
        context.updateCurrentNode(instance, node.getId());
        
        // 对每个分支进行处理
        log.info("并行节点处理: 节点={}, 分支数={}", node.getName(), outgoingFlows.size());
        for (FlowDTO flow : outgoingFlows) {
            context.processNextNode(instance, definitionDTO, flow.getTo());
        }
    }
    
    /**
     * 结束流程
     */
    private void completeProcess(NodeHandlerContext context, WfProcessInstance instance) {
        context.getHistoryActivityService().recordCompleteActivity(
            instance.getId(),
            "end",
            "结束",
            "END",
            instance.getStartUser(),
            "流程结束"
        );
        
        instance.setStatus(2); // 已结束
        context.getProcessInstanceMapper().updateById(instance);
    }
}
