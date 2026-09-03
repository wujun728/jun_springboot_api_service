package com.zapi.workflow.service.support.handler;

import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfCcRecord;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.service.WfCcRecordService;
import com.zapi.workflow.service.support.AbstractNodeHandler;
import com.zapi.workflow.service.support.NodeHandlerContext;
import com.zapi.workflow.service.support.message.WorkflowMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抄送节点处理器
 * 处理抄送通知，不生成审批任务，直接流转到下一节点
 * 支持多人/部门/角色抄送
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CcNodeHandler extends AbstractNodeHandler {
    
    private final WorkflowMessageHelper messageHelper;
    private final WfCcRecordService ccRecordService;
    
    @Override
    public String getNodeType() {
        return ProcessConstants.NodeType.CC;
    }
    
    @Override
    protected void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                            ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        String notifyType = StringUtils.hasText(node.getNotifyType()) ? node.getNotifyType() : "SYSTEM";
        
        // 处理抄送目标
        WfCcRecord ccRecord = null;
        String ccUserNames = "";
        List<Long> ccUserIds = Collections.emptyList();
        
        // 优先使用新的ccTargets配置（支持多人/部门/角色）
        if (!CollectionUtils.isEmpty(node.getCcTargets())) {
            // 转换为Service层的CcTarget
            List<WfCcRecordService.CcTarget> targets = node.getCcTargets().stream()
                .map(t -> new WfCcRecordService.CcTarget(t.getId(), t.getType(), t.getName()))
                .collect(Collectors.toList());
            
            // 创建抄送记录（一个节点一条记录）
            ccRecord = ccRecordService.createCcRecord(
                instance.getId(),
                instance.getProcessDefId(),
                node.getId(),
                node.getName(),
                targets,
                notifyType,
                node.getRequireRead()
            );
            
            if (ccRecord != null) {
                ccUserNames = ccRecord.getCcUserNames();
                ccUserIds = ccRecordService.expandCcTargetsToUserIds(targets);
            }
            
            log.info("抄送节点处理完成: 节点={}, 抄送人: {}", node.getName(), ccUserNames);
        } else {
            // 处理动态类型（DYNAMIC_USER、DYNAMIC_DEPT、DYNAMIC_ROLE）或旧的单人assignee配置
            String assigneeType = node.getAssigneeType();
            String assignee = context.resolveAssignee(node, instance);
            
            if (StringUtils.hasText(assignee)) {
                List<WfCcRecordService.CcTarget> targets = parseDynamicAssignee(assignee, assigneeType);
                
                if (!targets.isEmpty()) {
                    ccRecord = ccRecordService.createCcRecord(
                        instance.getId(),
                        instance.getProcessDefId(),
                        node.getId(),
                        node.getName(),
                        targets,
                        notifyType,
                        node.getRequireRead()
                    );
                    if (ccRecord != null) {
                        ccUserNames = ccRecord.getCcUserNames();
                        ccUserIds = ccRecordService.expandCcTargetsToUserIds(targets);
                    }
                    
                    log.info("动态抄送人解析成功: 节点={}, 类型={}, 抄送人: {}", 
                        node.getName(), assigneeType, ccUserNames);
                }
            }
        }
        
        // 构建抄送记录备注（用于流程历史显示）
        StringBuilder comment = new StringBuilder("抄送通知，方式: ").append(notifyType);
        if (StringUtils.hasText(ccUserNames)) {
            comment.append("，接收人: ").append(ccUserNames);
        }
        if (Boolean.TRUE.equals(node.getRequireRead())) {
            comment.append("，需确认阅读");
        }
        
        // 记录抄送节点完成
        context.getHistoryActivityService().recordCompleteActivity(
            instance.getId(),
            node.getId(),
            node.getName(),
            node.getType(),
            instance.getStartUser(),
            comment.toString()
        );
        
        // 发送抄送消息通知（给每个抄送人发送）
        for (Long userId : ccUserIds) {
            try {
                messageHelper.sendCcMessage(node, instance, definitionDTO, String.valueOf(userId));
            } catch (Exception e) {
                log.warn("发送抄送消息失败: node={}, userId={}, error={}", 
                    node.getId(), userId, e.getMessage());
                // 消息发送失败不影响主流程
            }
        }
        
        // 查找下一个节点
        String nextNodeId = context.findSingleNextNode(definitionDTO, node.getId());
        if (nextNodeId == null) {
            // 没有下一个节点，完成流程
            context.processNextNode(instance, definitionDTO, "end");
            return;
        }
        
        // 更新当前节点并处理下一个节点
        context.updateCurrentNode(instance, nextNodeId);
        context.processNextNode(instance, definitionDTO, nextNodeId);
    }
    
    /**
     * 解析动态抄送人配置
     * 
     * @param resolvedValue 解析后的值，可能是：
     *                      - 用户ID列表：1,2,3
     *                      - 部门前缀：DEPT:1,2,3
     *                      - 角色前缀：ROLE:1,2,3
     * @param assigneeType 抄送人类型
     * @return 抄送目标列表
     */
    private List<WfCcRecordService.CcTarget> parseDynamicAssignee(String resolvedValue, String assigneeType) {
        java.util.ArrayList<WfCcRecordService.CcTarget> targets = new java.util.ArrayList<>();
        
        if (resolvedValue == null || resolvedValue.isEmpty()) {
            return targets;
        }
        
        String type = "user"; // 默认用户类型
        String ids = resolvedValue;
        
        // 检查前缀
        if (resolvedValue.startsWith("DEPT:")) {
            type = "dept";
            ids = resolvedValue.substring(5);
        } else if (resolvedValue.startsWith("ROLE:")) {
            type = "role";
            ids = resolvedValue.substring(5);
        } else if ("DYNAMIC_DEPT".equals(assigneeType)) {
            type = "dept";
        } else if ("DYNAMIC_ROLE".equals(assigneeType)) {
            type = "role";
        }
        
        // 解析ID列表
        String[] idArray = ids.split(",");
        for (String idStr : idArray) {
            try {
                Long id = Long.parseLong(idStr.trim());
                targets.add(new WfCcRecordService.CcTarget(id, type, null));
            } catch (NumberFormatException e) {
                log.warn("解析动态抄送人ID失败: {}", idStr);
            }
        }
        
        return targets;
    }
}
