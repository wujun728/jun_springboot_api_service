package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 流程实例显示DTO
 * 用于任务列表展示，包含关联的流程定义和用户信息
 */
@Data
public class ProcessInstanceDisplayDTO {
    
    /**
     * 流程实例ID
     */
    private String id;
    
    /**
     * 流程定义ID
     */
    private String processDefId;
    
    /**
     * 流程标识
     */
    private String processKey;
    
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
     * 流程状态
     */
    private Integer status;
    
    /**
     * 当前节点ID（审批节点）
     */
    private String currentNodeId;
    
    /**
     * 当前节点名称
     */
    private String currentNodeName;
    
    /**
     * 流程启动时间
     */
    private LocalDateTime startTime;
    
    /**
     * 流程结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 任务优先级（来自流程定义）
     */
    private Integer priority;
    
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
     * 流程变量
     */
    private String variables;
    
    /**
     * 流程定义版本记录ID
     * 用于标识流程实例基于哪个版本的流程定义创建
     */
    private String processDefVersionId;
}
