package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 已办任务显示DTO
 * 用于已办任务列表展示，包含关联的流程定义和流程实例信息
 */
@Data
public class CompletedTaskDisplayDTO {
    
    /**
     * 历史活动ID
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
     * 流程名称（来自流程定义）
     */
    private String processName;
    
    /**
     * 表单名称（根据businessType组装）
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
     * 操作人ID
     */
    private String operator;
    
    /**
     * 操作人姓名（来自用户表）
     */
    private String operatorName;
    
    /**
     * 流程标识
     */
    private String processKey;
    
    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 任务完成时间
     */
    private LocalDateTime endTime;
    
    /**
     * 任务处理时长（毫秒）
     */
    private Long duration;
    
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
     * 操作备注
     */
    private String comment;
    
    /**
     * 流程状态
     */
    private Integer processStatus;
    
    /**
     * 优先级
     */
    private Integer priority;
}
