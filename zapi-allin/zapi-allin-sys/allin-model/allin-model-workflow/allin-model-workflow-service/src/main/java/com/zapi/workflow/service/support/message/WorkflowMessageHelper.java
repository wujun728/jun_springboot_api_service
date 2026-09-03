package com.zapi.workflow.service.support.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.entity.User;
import com.zapi.notification.entity.Message;
import com.zapi.notification.entity.ReceiverTarget;
import com.zapi.notification.service.MessageService;
import com.zapi.service.UserService;
import com.zapi.workflow.service.model.dto.MessageConfigDTO;
import com.zapi.workflow.service.model.dto.MessageConfigDTO.MessageTimingConfig;
import com.zapi.workflow.service.model.dto.MessageConfigDTO.ParamConfig;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.model.entity.WfTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作流消息发送工具类
 * 根据节点配置发送审批相关的消息通知
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowMessageHelper {
    
    private final MessageService messageService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    
    /**
     * 发送时机常量
     */
    public static final String TIMING_ON_PENDING = "onPending";
    public static final String TIMING_ON_APPROVE = "onApprove";
    public static final String TIMING_ON_REJECT = "onReject";
    public static final String TIMING_ON_COMPLETE = "onComplete";
    
    /**
     * 发送待审批通知
     * 当创建审批任务时调用，通知审批人有待办任务
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     * @param task 创建的任务
     */
    public void sendPendingMessage(NodeDTO node, WfProcessInstance instance, 
                                    ProcessDefinitionDTO definition, WfTask task) {
        Map<String, Object> extraContext = new HashMap<>();
        extraContext.put("assignee", getUserRealName(task.getAssignee()));
        extraContext.put("taskName", task.getNodeName());
        sendMessage(node, instance, definition, TIMING_ON_PENDING, task.getAssignee(), extraContext);
    }
    
    /**
     * 发送审批通过通知
     * 当审批人点击通过时调用，通知发起人审批已通过
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     * @param approver 审批人ID
     */
    public void sendApproveMessage(NodeDTO node, WfProcessInstance instance, 
                                    ProcessDefinitionDTO definition, String approver) {
        Map<String, Object> extraContext = new HashMap<>();
        extraContext.put("approver", getUserRealName(approver));
        extraContext.put("result", "通过");
        sendMessage(node, instance, definition, TIMING_ON_APPROVE, instance.getStartUser(), extraContext);
    }
    
    /**
     * 发送审批驳回通知
     * 当审批人点击驳回时调用，通知发起人审批被驳回
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     * @param approver 审批人ID
     */
    public void sendRejectMessage(NodeDTO node, WfProcessInstance instance, 
                                   ProcessDefinitionDTO definition, String approver) {
        Map<String, Object> extraContext = new HashMap<>();
        extraContext.put("approver", getUserRealName(approver));
        extraContext.put("result", "驳回");
        sendMessage(node, instance, definition, TIMING_ON_REJECT, instance.getStartUser(), extraContext);
    }
    
    /**
     * 发送审批完成通知
     * 当审批完成（无论通过还是驳回）时调用
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     */
    public void sendCompleteMessage(NodeDTO node, WfProcessInstance instance, 
                                     ProcessDefinitionDTO definition) {
        sendMessage(node, instance, definition, TIMING_ON_COMPLETE, instance.getStartUser(), null);
    }
    
    /**
     * 发送抄送通知
     * 当流程执行到抄送节点时调用，通知抄送人
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     * @param ccReceiver 抄送接收人
     */
    public void sendCcMessage(NodeDTO node, WfProcessInstance instance, 
                               ProcessDefinitionDTO definition, String ccReceiver) {
        // 抄送节点的消息配置结构与审批节点不同，使用简化结构
        sendCcMessageInternal(node, instance, definition, ccReceiver);
    }
    
    /**
     * 发送抄送消息内部实现
     */
    private void sendCcMessageInternal(NodeDTO node, WfProcessInstance instance, 
                                        ProcessDefinitionDTO definition, String receiverId) {
        // 获取抄送节点的消息配置（简化结构）
        MessageConfigDTO config = node.getMessageConfig();
        if (config == null) {
            return;
        }
        
        // 检查是否启用
        if (!Boolean.TRUE.equals(config.getEnabled())) {
            return;
        }
        
        try {
            // 构建参数上下文
            Map<String, Object> extraContext = new HashMap<>();
            extraContext.put("ccReceiver", getUserRealName(receiverId));
            Map<String, String> paramContext = buildParamContext(config, instance, definition, node, extraContext);
            
            // 获取并替换变量
            String title = replaceVariables(config.getTitle(), paramContext);
            String content = replaceVariables(config.getContent(), paramContext);
            
            if (title == null || title.trim().isEmpty()) {
                title = "抄送通知";
            }
            if (content == null || content.trim().isEmpty()) {
                content = "您有一条抄送消息";
            }
            
            // 创建消息
            Message message = new Message();
            message.setTitle(title);
            message.setContent(content);
            message.setSenderId(0L);
            message.setMessageTag("WORKFLOW_CC");
            
            // 设置接收人
            Long receiverIdLong = parseUserId(receiverId);
            if (receiverIdLong != null) {
                ReceiverTarget target = new ReceiverTarget();
                target.setId(receiverIdLong);
                target.setType("user");
                message.setReceiverTargets(Collections.singletonList(target));
                
                boolean success = messageService.sendMessage(message);
                if (success) {
                    log.info("抄送消息发送成功: receiver={}, title={}", receiverId, title);
                } else {
                    log.warn("抄送消息发送失败: receiver={}", receiverId);
                }
            }
            
        } catch (Exception e) {
            log.error("发送抄送消息异常: node={}, error={}", node.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 通用消息发送方法
     *
     * @param node 节点配置
     * @param instance 流程实例
     * @param definition 流程定义
     * @param timing 发送时机
     * @param receiverId 接收人ID
     * @param extraContext 额外的上下文参数
     */
    private void sendMessage(NodeDTO node, WfProcessInstance instance, 
                              ProcessDefinitionDTO definition, String timing, 
                              String receiverId, Map<String, Object> extraContext) {
        MessageConfigDTO messageConfig = node.getMessageConfig();
        if (messageConfig == null) {
            return;
        }
        
        MessageTimingConfig timingConfig = messageConfig.getTimingConfig(timing);
        if (timingConfig == null || !timingConfig.isEnabled()) {
            return;
        }
        
        try {
            // 构建参数上下文
            Map<String, String> paramContext = buildParamContext(messageConfig, instance, definition, node, extraContext);
            
            // 替换消息中的变量
            String title = replaceVariables(timingConfig.getTitle(), paramContext);
            String content = replaceVariables(timingConfig.getContent(), paramContext);
            
            if (title == null || title.trim().isEmpty()) {
                title = "审批通知";
            }
            if (content == null || content.trim().isEmpty()) {
                content = "您有一条审批消息";
            }
            
            // 创建消息
            Message message = new Message();
            message.setTitle(title);
            message.setContent(content);
            message.setSenderId(0L); // 系统发送
            message.setMessageTag("WORKFLOW"); // 工作流消息标签
            
            // 设置接收人
            Long receiverIdLong = parseUserId(receiverId);
            if (receiverIdLong != null) {
                ReceiverTarget target = new ReceiverTarget();
                target.setId(receiverIdLong);
                target.setType("user");
                message.setReceiverTargets(Collections.singletonList(target));
                
                // 发送消息
                boolean success = messageService.sendMessage(message);
                if (success) {
                    log.info("工作流消息发送成功: timing={}, receiver={}, title={}", timing, receiverId, title);
                } else {
                    log.warn("工作流消息发送失败: timing={}, receiver={}", timing, receiverId);
                }
            } else {
                log.warn("无法解析接收人ID: {}", receiverId);
            }
            
        } catch (Exception e) {
            log.error("发送工作流消息异常: timing={}, node={}, error={}", timing, node.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 构建参数上下文
     * 根据配置的参数来源获取实际值
     */
    private Map<String, String> buildParamContext(MessageConfigDTO config, WfProcessInstance instance,
                                                   ProcessDefinitionDTO definition, NodeDTO node,
                                                   Map<String, Object> extraContext) {
        Map<String, String> context = new HashMap<>();
        
        // === 添加内置默认参数 ===
        
        // 流程相关
        context.put("flowName", definition.getName() != null ? definition.getName() : "");
        context.put("processName", definition.getName() != null ? definition.getName() : "");
        context.put("nodeName", node.getName() != null ? node.getName() : "");
        context.put("processKey", instance.getProcessKey() != null ? instance.getProcessKey() : "");
        context.put("businessKey", instance.getBusinessKey() != null ? instance.getBusinessKey() : "");
        context.put("processInstanceId", instance.getId() != null ? instance.getId().toString() : "");
        
        // 发起人信息
        String startUserName = getUserRealName(instance.getStartUser());
        context.put("startUser", startUserName);
        context.put("initiator", startUserName);
        context.put("startUserName", startUserName);
        context.put("applicant", startUserName);
        
        // 时间相关
        if (instance.getStartTime() != null) {
            context.put("startTime", instance.getStartTime().toString());
        }
        
        // === 添加额外上下文参数 ===
        if (extraContext != null) {
            for (Map.Entry<String, Object> entry : extraContext.entrySet()) {
                Object value = entry.getValue();
                context.put(entry.getKey(), value != null ? value.toString() : "");
            }
        }
        
        // === 解析流程变量 ===
        Map<String, Object> processVariables = parseProcessVariables(instance);
        
        // 将所有流程变量添加到上下文（优先级较低，不覆盖已有的）
        for (Map.Entry<String, Object> entry : processVariables.entrySet()) {
            if (!context.containsKey(entry.getKey())) {
                Object value = entry.getValue();
                context.put(entry.getKey(), value != null ? value.toString() : "");
            }
        }
        
        // === 处理用户配置的参数（最高优先级） ===
        Map<String, ParamConfig> params = config.getParams();
        if (params != null) {
            for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
                String paramName = entry.getKey();
                ParamConfig paramConfig = entry.getValue();
                String value = resolveParamValue(paramConfig, processVariables);
                if (value != null) {
                    context.put(paramName, value);
                }
            }
        }
        
        return context;
    }
    
    /**
     * 解析参数值
     */
    private String resolveParamValue(ParamConfig paramConfig, Map<String, Object> processVariables) {
        if (paramConfig == null || paramConfig.getSourceType() == null) {
            return null;
        }
        
        String sourceType = paramConfig.getSourceType();
        String configValue = paramConfig.getValue();
        
        switch (sourceType) {
            case "FIXED":
                return configValue;
            case "VARIABLE":
                if (configValue != null && processVariables.containsKey(configValue)) {
                    Object val = processVariables.get(configValue);
                    return val != null ? val.toString() : "";
                }
                return "";
            case "FORM_FIELD":
                // 表单字段也从流程变量中获取
                if (configValue != null && processVariables.containsKey(configValue)) {
                    Object val = processVariables.get(configValue);
                    return val != null ? val.toString() : "";
                }
                return "";
            default:
                return configValue;
        }
    }
    
    /**
     * 解析流程变量
     */
    private Map<String, Object> parseProcessVariables(WfProcessInstance instance) {
        if (instance.getVariables() == null || instance.getVariables().isEmpty()) {
            return new HashMap<>();
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = objectMapper.readValue(instance.getVariables(), Map.class);
            return variables != null ? variables : new HashMap<>();
        } catch (Exception e) {
            log.warn("解析流程变量失败: {}", e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * 替换消息中的变量
     * 将 ${xxx} 格式的变量替换为实际值
     */
    private String replaceVariables(String template, Map<String, String> context) {
        if (template == null || template.isEmpty()) {
            return template;
        }
        
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = context.getOrDefault(varName, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    /**
     * 获取用户真实姓名
     */
    private String getUserRealName(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return "未知用户";
        }
        try {
            Long userIdLong = Long.parseLong(userId);
            User user = userService.getById(userIdLong);
            if (user != null && user.getRealName() != null && !user.getRealName().trim().isEmpty()) {
                return user.getRealName();
            }
        } catch (NumberFormatException e) {
            // 尝试通过用户名查询
            User user = userService.findByUsername(userId);
            if (user != null && user.getRealName() != null) {
                return user.getRealName();
            }
        } catch (Exception e) {
            log.warn("获取用户姓名失败: userId={}, error={}", userId, e.getMessage());
        }
        return userId;
    }
    
    /**
     * 解析用户ID为Long类型
     */
    private Long parseUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            // 尝试通过用户名查询用户ID
            try {
                User user = userService.findByUsername(userId);
                if (user != null) {
                    return user.getId();
                }
            } catch (Exception ex) {
                log.warn("通过用户名查询用户失败: username={}", userId);
            }
            return null;
        }
    }
}
