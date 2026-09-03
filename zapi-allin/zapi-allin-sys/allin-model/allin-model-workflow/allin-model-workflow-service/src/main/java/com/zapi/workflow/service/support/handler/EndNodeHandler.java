package com.zapi.workflow.service.support.handler;

import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.support.AbstractNodeHandler;
import com.zapi.workflow.service.support.NodeHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 结束节点处理器
 * 完成流程实例，设置结束状态
 */
@Slf4j
@Component
public class EndNodeHandler extends AbstractNodeHandler {
    
    @Override
    public String getNodeType() {
        return ProcessConstants.NodeType.END;
    }
    
    @Override
    protected void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                            ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        // 记录结束节点历史
        context.getHistoryActivityService().recordCompleteActivity(
            instance.getId(),
            node != null ? node.getId() : "end",
            node != null ? node.getName() : "结束",
            "END",
            instance.getStartUser(),
            "流程结束"
        );
        
        // 更新流程实例状态
        instance.setStatus(2); // 已结束
        instance.setEndTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        context.getProcessInstanceMapper().updateById(instance);
        
        log.info("流程实例已结束: instanceId={}", instance.getId());
    }
}
