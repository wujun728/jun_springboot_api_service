package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.entity.User;
import com.zapi.service.UserService;
import com.zapi.workflow.service.mapper.WfTaskMapper;
import com.zapi.workflow.service.model.dto.CompleteTaskDTO;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.dto.TaskDisplayDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.model.entity.WfTask;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import com.zapi.workflow.service.service.WfProcessInstanceService;
import com.zapi.workflow.service.service.WfTaskService;
import com.zapi.workflow.service.service.WfHistoryActivityService;
import com.zapi.workflow.service.service.WfApprovalRecordService;
import com.zapi.workflow.service.support.NodeHandlerRegistry;
import com.zapi.workflow.service.support.message.WorkflowMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WfTaskServiceImpl extends ServiceImpl<WfTaskMapper, WfTask> implements WfTaskService {

    private final WfProcessInstanceService processInstanceService;
    private final WfProcessDefinitionService processDefinitionService;
    private final ObjectMapper objectMapper;
    private final WfHistoryActivityService historyActivityService;
    private final UserService userService;
    private final NodeHandlerRegistry nodeHandlerRegistry;
    private final WorkflowMessageHelper messageHelper;
    private final WfApprovalRecordService approvalRecordService;
    
    @Override
    @Transactional
    public void completeTask(CompleteTaskDTO completeTaskDTO) {
        // 1. 获取任务
        WfTask task = baseMapper.selectById(completeTaskDTO.getTaskId());
        if (task == null) {
            throw new RuntimeException("任务不存在: " + completeTaskDTO.getTaskId());
        }
        
        if (task.getStatus() != 1) { // 1-待处理
            throw new RuntimeException("任务状态不正确，无法完成: " + task.getStatus());
        }
        
        // 2. 获取流程实例
        WfProcessInstance instance = processInstanceService.getById(task.getProcessInstanceId());
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + task.getProcessInstanceId());
        }
        
        // 3. 获取流程定义
        ProcessDefinitionDTO definitionDTO = processDefinitionService.getDefinitionDTOById(instance.getProcessDefId());
        
        // 4. 更新任务状态
        task.setStatus(completeTaskDTO.isApproved() ? 2 : 3); // 2-已完成 3-已驳回
        task.setCompleteTime(LocalDateTime.now());
        task.setCompleteUser(completeTaskDTO.getOperator());
        task.setComment(completeTaskDTO.getComment());
        baseMapper.updateById(task);
        
        // 4.1 构建审批意见
        String comment = completeTaskDTO.isApproved() ? 
            "审批通过：" + completeTaskDTO.getComment() : 
            "审批驳回：" + completeTaskDTO.getComment();
        
        // 4.2 记录审批结果到审批记录表（会签/或签逻辑）
        Boolean approvalResult = approvalRecordService.recordApprovalResult(
            task.getProcessInstanceId(),
            task.getNodeId(),
            completeTaskDTO.isApproved()
        );
        
        // 4.3 根据审批结果判断是否继续流程
        if (approvalResult == null) {
            // 会签模式下，还需要等待其他人审批
            // 为当前审批人创建独立的历史记录
            historyActivityService.recordCompleteActivity(
                task.getProcessInstanceId(),
                task.getNodeId(),
                task.getNodeName(),
                "APPROVAL",
                completeTaskDTO.getOperator(),
                comment
            );
            log.info("会签模式：等待其他审批人，taskId={}, nodeId={}", task.getId(), task.getNodeId());
            return;
        }
        
        // 4.4 节点审批完成（通过或驳回）
        // 检查是否是多人审批（会签场景）
        boolean isMultiApproval = approvalRecordService.isMultiApproval(task.getProcessInstanceId(), task.getNodeId());
        
        if (isMultiApproval) {
            // 多人审批：为当前审批人创建独立的历史记录
            historyActivityService.recordCompleteActivity(
                task.getProcessInstanceId(),
                task.getNodeId(),
                task.getNodeName(),
                "APPROVAL",
                completeTaskDTO.getOperator(),
                comment
            );
            // 更新节点开始记录的结束时间（标记节点完成）
            String nodeEndComment = approvalResult ? "审批节点完成：通过" : "审批节点完成：驳回";
            historyActivityService.recordActivityEnd(
                task.getProcessInstanceId(),
                task.getNodeId(),
                completeTaskDTO.getOperator(),
                nodeEndComment
            );
        } else {
            // 单人审批：直接更新节点开始记录
            historyActivityService.recordActivityEnd(
                task.getProcessInstanceId(),
                task.getNodeId(),
                completeTaskDTO.getOperator(),
                comment
            );
        }
        
        // 4.5 取消该节点的其他待处理任务
        cancelOtherPendingTasksForNode(task.getProcessInstanceId(), task.getNodeId(), task.getId());
        
        // 5. 如果是驳回，结束流程
        if (!approvalResult) {
            instance.setStatus(2); // 已结束
            instance.setEndTime(LocalDateTime.now());
            processInstanceService.updateById(instance);
            
            // 发送审批驳回通知
            sendApprovalMessage(task, instance, definitionDTO, completeTaskDTO.getOperator(), false);
            return;
        }
        
        // 发送审批通过通知
        sendApprovalMessage(task, instance, definitionDTO, completeTaskDTO.getOperator(), true);
        
        // 6. 更新流程变量
        updateProcessVariables(instance, completeTaskDTO.getVariables());
        
        // 7. 找到当前节点的流出连线
        List<com.zapi.workflow.service.model.dto.FlowDTO> outgoingFlows = definitionDTO.getFlows().stream()
                .filter(flow -> task.getNodeId().equals(flow.getFrom()))
                .collect(Collectors.toList());
        
        if (outgoingFlows.isEmpty()) {
            // 没有流出连线，流程结束
            instance.setStatus(2); // 已结束
            instance.setEndTime(LocalDateTime.now());
            processInstanceService.updateById(instance);
            return;
        }
        
        // 8. 获取下一个节点ID（审批节点通常只有一条流出连线）
        String nextNodeId = outgoingFlows.get(0).getTo();
        
        // 9. 更新流程实例当前节点
        instance.setCurrentNodeId(nextNodeId);
        processInstanceService.updateById(instance);
        
        // 10. 处理下一个节点（使用策略模式）
        nodeHandlerRegistry.processNextNode(instance, definitionDTO, nextNodeId);
    }

    @Override
    public IPage<WfTask> getUserTasks(Page<WfTask> page, String assignee, String appId) {
        // 如果传递了 appId，需要通过 JOIN 流程实例表来过滤
        if (StringUtils.hasText(appId)) {
            return baseMapper.selectUserTasksByAppId(page, assignee, 1, appId);
        } else {
            // 如果没有传递 appId，只查询 app_id 为 NULL 的任务
            return baseMapper.selectUserTasksWithNullAppId(page, assignee, 1);
        }
    }
    
    /**
     * 更新流程变量
     */
    private void updateProcessVariables(WfProcessInstance instance, Map<String, Object> newVariables) {
        if (newVariables == null || newVariables.isEmpty()) {
            return;
        }
        
        try {
             // 1. 解析现有变量
             Map<String, Object> variables = null;
             if (instance.getVariables() != null) {
                 @SuppressWarnings("unchecked")
                 Map<String, Object> parsedVariables = objectMapper.readValue(instance.getVariables(), Map.class);
                 variables = parsedVariables;
             }
            
            // 2. 合并新变量
            if (variables == null) {
                variables = newVariables;
            } else {
                variables.putAll(newVariables);
            }
            
            // 3. 保存更新后的变量
            instance.setVariables(objectMapper.writeValueAsString(variables));
            processInstanceService.updateById(instance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("更新流程变量失败", e);
        }
    }
    @Override
    public IPage<TaskDisplayDTO> getUserTasksWithDisplayInfo(Page<TaskDisplayDTO> page, String assignee, String appId) {
        // 1. 先查询基础任务信息
        Page<WfTask> taskPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<WfTask> taskResult = getUserTasks(taskPage, assignee, appId);

        // 2. 转换为显示DTO并填充关联信息
        List<TaskDisplayDTO> displayList = taskResult.getRecords().stream()
                .map(this::convertToTaskDisplayDTO)
                .collect(Collectors.toList());

        // 3. 构建分页结果
        Page<TaskDisplayDTO> resultPage = new Page<>(page.getCurrent(), page.getSize(), taskResult.getTotal());
        resultPage.setRecords(displayList);

        return resultPage;
    }
    
    /**
     * 转换任务为显示DTO
     */
    private TaskDisplayDTO convertToTaskDisplayDTO(WfTask task) {
        TaskDisplayDTO dto = new TaskDisplayDTO();
        
        // 基础任务信息
        dto.setId(task.getId());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefVersionId(task.getProcessDefVersionId());
        dto.setNodeId(task.getNodeId());
        dto.setNodeName(task.getNodeName());
        dto.setAssignee(task.getAssignee());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setCreateTime(task.getCreateTime());
        dto.setCompleteTime(task.getCompleteTime());
        dto.setVariables(task.getVariables());
        
        try {
            // 获取流程实例信息
            WfProcessInstance instance = processInstanceService.getById(task.getProcessInstanceId());
            if (instance != null) {
                dto.setProcessDefId(instance.getProcessDefId());
                dto.setProcessKey(instance.getProcessKey());
                dto.setBusinessKey(instance.getBusinessKey());
                dto.setStartUser(instance.getStartUser());
                dto.setProcessStartTime(instance.getStartTime());
                
                // 获取流程定义信息
                ProcessDefinitionDTO definition = processDefinitionService.getDefinitionDTOById(instance.getProcessDefId());
                if (definition != null) {
                    // 任务名称：使用流程定义的名称
                    dto.setTaskName(definition.getName());
                    
                    // 表单名称：根据业务类型判断
                    dto.setBusinessType(definition.getBusinessType());
                    dto.setSystemFormKey(definition.getSystemFormKey());
                    dto.setBusinessFormId(definition.getBusinessFormId());
                    
                    if (definition.getBusinessType() == 1) {
                        // 系统表单
                        String formName = definition.getSystemFormKey() != null ? 
                            "系统表单(" + definition.getSystemFormKey() + ")" : "系统表单(未配置)";
                        dto.setFormName(formName);
                    } else if (definition.getBusinessType() == 2) {
                        // 自定义表单
                        String formName = definition.getBusinessFormId() != null ? 
                            "自定义表单(" + definition.getBusinessFormId() + ")" : "自定义表单(未配置)";
                        dto.setFormName(formName);
                    } else {
                        dto.setFormName("未配置表单");
                    }
                } else {
                    // 流程定义不存在时的默认值
                    dto.setTaskName(instance.getProcessKey());
                    dto.setFormName("未配置表单");
                }
                
                // 获取发起人真实姓名
                dto.setStartUserName(getUserRealName(instance.getStartUser()));
                
                // 获取审批人真实姓名
                dto.setAssigneeName(getUserRealName(task.getAssignee()));
                
            } else {
                // 流程实例不存在时的默认值
                dto.setTaskName("未知任务");
                dto.setFormName("加载失败");
                dto.setStartUserName("未知");
                dto.setAssigneeName("未知");
            }
            
        } catch (Exception e) {
            // 异常时使用默认值
            dto.setTaskName("未知任务");
            dto.setFormName("加载失败");
            dto.setStartUserName("未知");
            dto.setAssigneeName(getUserRealName(task.getAssignee()));
        }
        
        return dto;
    }
    
    /**
     * 根据用户ID获取真实姓名
     */
    private String getUserRealName(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return "未知用户";
        }
        
        try {
            // 尝试将userId转换为Long类型
            Long userIdLong = Long.parseLong(userId);
            User user = userService.getById(userIdLong);
            if (user != null && user.getRealName() != null && !user.getRealName().trim().isEmpty()) {
                return user.getRealName();
            }
        } catch (NumberFormatException e) {
            // 如果userId不是数字，尝试通过用户名查询
            User user = userService.findByUsername(userId);
            if (user != null && user.getRealName() != null && !user.getRealName().trim().isEmpty()) {
                return user.getRealName();
            }
        } catch (Exception e) {
            // 查询失败，返回用户ID
        }
        
        // 如果查询失败或没有真实姓名，返回用户ID
        return userId;
    }
    
    /**
     * 发送审批结果消息
     * 根据节点配置发送审批通过或驳回消息
     */
    private void sendApprovalMessage(WfTask task, WfProcessInstance instance, 
                                      ProcessDefinitionDTO definitionDTO, 
                                      String approver, boolean approved) {
        try {
            // 找到当前节点的配置
            NodeDTO node = findNodeById(definitionDTO, task.getNodeId());
            if (node == null) {
                return;
            }
            
            if (approved) {
                // 发送审批通过通知
                messageHelper.sendApproveMessage(node, instance, definitionDTO, approver);
            } else {
                // 发送审批驳回通知
                messageHelper.sendRejectMessage(node, instance, definitionDTO, approver);
            }
        } catch (Exception e) {
            // 消息发送失败不影响主流程
            log.warn("发送审批消息失败: taskId={}, error={}", task.getId(), e.getMessage());
        }
    }
    
    /**
     * 根据节点ID找到节点配置
     */
    private NodeDTO findNodeById(ProcessDefinitionDTO definitionDTO, String nodeId) {
        if (definitionDTO.getNodes() == null) {
            return null;
        }
        return definitionDTO.getNodes().stream()
                .filter(n -> nodeId.equals(n.getId()))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    @Transactional
    public void cancelTasksByInstanceId(String instanceId) {
        // 查找该流程实例下所有待处理的任务
        LambdaQueryWrapper<WfTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfTask::getProcessInstanceId, instanceId)
                   .eq(WfTask::getStatus, 1); // 1-待处理
        
        List<WfTask> pendingTasks = baseMapper.selectList(queryWrapper);
        
        if (pendingTasks != null && !pendingTasks.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            for (WfTask task : pendingTasks) {
                // 将任务状态改为已取消（使用 4 表示已取消）
                task.setStatus(4);
                task.setCompleteTime(now);
                task.setComment("流程已被发起人撤回");
                baseMapper.updateById(task);
            }
            log.info("已取消流程实例 [{}] 的 {} 个待办任务", instanceId, pendingTasks.size());
        }
    }
    
    /**
     * 取消同一节点的其他待处理任务
     * 用于会签/或签场景：当节点审批完成后，取消该节点的其他待处理任务
     * 
     * @param instanceId 流程实例ID
     * @param nodeId 节点ID
     * @param currentTaskId 当前完成的任务ID（不取消）
     */
    @Transactional
    public void cancelOtherPendingTasksForNode(String instanceId, String nodeId, String currentTaskId) {
        LambdaQueryWrapper<WfTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfTask::getProcessInstanceId, instanceId)
                   .eq(WfTask::getNodeId, nodeId)
                   .eq(WfTask::getStatus, 1) // 1-待处理
                   .ne(WfTask::getId, currentTaskId); // 排除当前任务
        
        List<WfTask> pendingTasks = baseMapper.selectList(queryWrapper);
        
        if (pendingTasks != null && !pendingTasks.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            for (WfTask task : pendingTasks) {
                task.setStatus(4); // 4-已取消
                task.setCompleteTime(now);
                task.setComment("节点审批已完成，其他待处理任务自动取消");
                baseMapper.updateById(task);
            }
            log.info("已取消节点 [{}] 的 {} 个其他待办任务", nodeId, pendingTasks.size());
        }
    }
}