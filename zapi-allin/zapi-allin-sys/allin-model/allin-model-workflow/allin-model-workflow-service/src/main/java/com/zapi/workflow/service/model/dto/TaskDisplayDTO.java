package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待办任务显示DTO
 * 用于待办任务列表展示，包含关联的流程定义和流程实例信息
 */
@Data
public class TaskDisplayDTO {
    
    /**
     * 任务ID
     */
    private String id;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 流程定义ID
     */
    private String processDefId;
    
    /**
     * 节点ID
     */
    private String nodeId;
    
    /**
     * 节点名称
     */
    private String nodeName;
    
    /**
     * 任务名称（来自流程定义的name）
     */
    private String taskName;
    
    /**
     * 任务表单名称（根据businessType组装）
     */
    private String formName;
    
    /**
     * 业务关联ID
     */
    private String businessKey;
    
    /**
     * 发起人用户名（来自用户表）
     */
    private String startUserName;
    
    /**
     * 发起人ID
     */
    private String startUser;
    
    /**
     * 任务处理人ID
     */
    private String assignee;
    
    /**
     * 任务处理人姓名（来自用户表）
     */
    private String assigneeName;
    
    /**
     * 任务状态
     */
    private Integer status;
    
    /**
     * 任务优先级
     */
    private Integer priority;
    
    /**
     * 流程标识
     */
    private String processKey;
    
    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 任务完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 流程启动时间
     */
    private LocalDateTime processStartTime;
    
    /**
     * 业务类型（来自流程定义）
     */
    private Integer businessType;
    
    /**
     * 系统表单标识（来自流程定义）
     */
    private String systemFormKey;
    
    /**
     * 自定义表单ID（来自流程定义）
     */
    private String businessFormId;
    
    /**
     * 任务变量
     */
    private String variables;
    
    /**
     * 流程定义版本记录ID
     * 用于标识任务基于哪个版本的流程定义创建
     */
    private String processDefVersionId;
}
