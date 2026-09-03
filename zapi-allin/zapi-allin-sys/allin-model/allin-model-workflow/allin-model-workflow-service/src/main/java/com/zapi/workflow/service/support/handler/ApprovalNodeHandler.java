package com.zapi.workflow.service.support.handler;

import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfApprovalRecord;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.model.entity.WfTask;
import com.zapi.workflow.service.service.WfApprovalRecordService;
import com.zapi.workflow.service.service.WfCcRecordService;
import com.zapi.workflow.service.support.AbstractNodeHandler;
import com.zapi.workflow.service.support.NodeHandlerContext;
import com.zapi.workflow.service.support.message.WorkflowMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批节点处理器
 * 处理审批类型节点，创建审批任务并发送待审批通知
 * 支持多人/部门/角色审批
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalNodeHandler extends AbstractNodeHandler {
    
    private final WorkflowMessageHelper messageHelper;
    private final WfCcRecordService ccRecordService;
    private final WfApprovalRecordService approvalRecordService;
    
    @Override
    public String getNodeType() {
        return ProcessConstants.NodeType.APPROVAL;
    }
    
    @Override
    protected void beforeHandle(NodeHandlerContext context, WfProcessInstance instance,
                                ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        // 记录进入审批节点的历史
        context.getHistoryActivityService().recordActivityStart(
            instance.getId(),
            node.getId(),
            node.getName(),
            node.getType(),
            instance.getStartUser()
        );
    }
    
    @Override
    protected void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                            ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        
        // 获取所有审批人ID和目标配置
        List<WfCcRecordService.CcTarget> approvalTargets = new ArrayList<>();
        List<Long> approverIds = resolveApproverIds(context, node, instance, approvalTargets);
        
        if (approverIds.isEmpty()) {
            log.warn("审批节点没有配置审批人: 节点={}", node.getName());
            // 如果没有审批人，使用旧的单人模式
            String assignee = context.resolveAssignee(node, instance);
            if (assignee != null && !assignee.isEmpty()) {
                try {
                    Long userId = Long.parseLong(assignee);
                    approverIds.add(userId);
                    approvalTargets.add(new WfCcRecordService.CcTarget(userId, "user", null));
                } catch (NumberFormatException e) {
                    log.error("审批人ID格式错误: {}", assignee);
                }
            }
        }
        
        if (approverIds.isEmpty()) {
            log.error("审批节点没有有效的审批人: 节点={}", node.getName());
            return;
        }
        
        // 创建审批节点记录（一个节点一条记录）
        // 从节点配置中获取审批方式：ANY-或签，ALL-会签
        String approvalMode = node.getApprovalMode();
        if (approvalMode == null || approvalMode.isEmpty()) {
            approvalMode = "ANY"; // 默认或签
        }
        
        if (!approvalTargets.isEmpty()) {
            WfApprovalRecord approvalRecord = approvalRecordService.createApprovalRecord(
                instance.getId(),
                instance.getProcessDefId(),
                node.getId(),
                node.getName(),
                approvalTargets,
                approvalMode
            );
            if (approvalRecord != null) {
                log.info("创建审批记录成功: 节点={}, 审批人: {}, 审批方式: {}", 
                    node.getName(), approvalRecord.getApprovalUserNames(), approvalMode);
            }
        }
        
        // 为每个审批人创建任务
        for (Long approverId : approverIds) {
            WfTask task = new WfTask();
            task.setProcessInstanceId(instance.getId());
            // 关联流程定义版本记录ID，确保任务执行时使用启动时的流程定义版本
            task.setProcessDefVersionId(instance.getProcessDefVersionId());
            task.setNodeId(node.getId());
            task.setNodeName(node.getName());
            task.setAssignee(String.valueOf(approverId));
            task.setStatus(1); // 待处理
            task.setPriority(node.getPriority());
            task.setCreateTime(LocalDateTime.now());
            
            context.getTaskMapper().insert(task);
            
            log.info("创建审批任务成功: 节点={}, 审批人={}", node.getName(), approverId);
            
            // 发送待审批通知
            try {
                messageHelper.sendPendingMessage(node, instance, definitionDTO, task);
            } catch (Exception e) {
                log.warn("发送待审批消息失败: node={}, assignee={}, error={}", 
                    node.getId(), approverId, e.getMessage());
                // 消息发送失败不影响主流程
            }
        }
        
        log.info("审批节点处理完成: 节点={}, 创建任务数={}", node.getName(), approverIds.size());
    }
    
    /**
     * 解析审批人ID列表
     * 优先使用 approvalTargets（多人/部门/角色），否则使用旧的 assignee
     * 支持动态类型：DYNAMIC_USER、DYNAMIC_DEPT、DYNAMIC_ROLE
     * 
     * @param context 上下文
     * @param node 节点
     * @param instance 流程实例
     * @param outTargets 输出参数，返回审批目标配置
     * @return 审批人ID列表
     */
    private List<Long> resolveApproverIds(NodeHandlerContext context, NodeDTO node, 
            WfProcessInstance instance, List<WfCcRecordService.CcTarget> outTargets) {
        List<Long> approverIds = new ArrayList<>();
        
        // 优先使用新的 approvalTargets 配置（固定人员模式）
        if (!CollectionUtils.isEmpty(node.getApprovalTargets())) {
            // 转换为 CcTarget 格式（复用抄送的展开逻辑）
            List<WfCcRecordService.CcTarget> targets = node.getApprovalTargets().stream()
                .map(t -> new WfCcRecordService.CcTarget(t.getId(), t.getType(), t.getName()))
                .collect(Collectors.toList());
            
            // 保存目标配置
            outTargets.addAll(targets);
            
            // 展开获取所有实际用户ID
            approverIds = ccRecordService.expandCcTargetsToUserIds(targets);
            
            if (!approverIds.isEmpty()) {
                String userNames = ccRecordService.getUserNamesString(approverIds);
                log.info("审批节点展开审批人: 节点={}, 审批人: {}", node.getName(), userNames);
            }
            return approverIds;
        }
        
        // 处理动态类型（DYNAMIC_USER、DYNAMIC_DEPT、DYNAMIC_ROLE）
        String assigneeType = node.getAssigneeType();
        if (assigneeType != null && assigneeType.startsWith("DYNAMIC")) {
            String resolvedValue = context.resolveAssignee(node, instance);
            if (resolvedValue != null && !resolvedValue.isEmpty()) {
                List<WfCcRecordService.CcTarget> dynamicTargets = parseDynamicAssignee(resolvedValue, assigneeType);
                if (!dynamicTargets.isEmpty()) {
                    outTargets.addAll(dynamicTargets);
                    approverIds = ccRecordService.expandCcTargetsToUserIds(dynamicTargets);
                    
                    if (!approverIds.isEmpty()) {
                        String userNames = ccRecordService.getUserNamesString(approverIds);
                        log.info("动态审批人解析成功: 节点={}, 类型={}, 审批人: {}", 
                            node.getName(), assigneeType, userNames);
                    }
                }
            }
        }
        
        return approverIds;
    }
    
    /**
     * 解析动态审批人配置
     * 
     * @param resolvedValue 解析后的值，可能是：
     *                      - 用户ID列表：1,2,3
     *                      - 部门前缀：DEPT:1,2,3
     *                      - 角色前缀：ROLE:1,2,3
     * @param assigneeType 审批人类型
     * @return 审批目标列表
     */
    private List<WfCcRecordService.CcTarget> parseDynamicAssignee(String resolvedValue, String assigneeType) {
        List<WfCcRecordService.CcTarget> targets = new ArrayList<>();
        
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
                log.warn("解析动态审批人ID失败: {}", idStr);
            }
        }
        
        return targets;
    }
}
