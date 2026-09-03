package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 抄送记录显示DTO
 * 用于抄送列表展示，包含关联的流程定义和流程实例信息
 */
@Data
public class CcDisplayDTO {
    
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
     * 抄送接收人ID
     */
    private String ccUser;
    
    /**
     * 抄送接收人姓名（来自用户表）
     */
    private String ccUserName;
    
    /**
     * 抄送接收人姓名列表（多人时用顿号分隔）
     * 前端兼容字段
     */
    public String getCcUserNames() {
        return ccUserName;
    }
    
    public void setCcUserNames(String ccUserNames) {
        this.ccUserName = ccUserNames;
    }
    
    /**
     * 通知方式
     */
    private String notifyType;
    
    /**
     * 是否需要确认阅读
     */
    private Boolean requireRead;
    
    /**
     * 流程标识
     */
    private String processKey;
    
    /**
     * 抄送时间
     */
    private LocalDateTime ccTime;
    
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
     * 抄送备注
     */
    private String comment;
    
    /**
     * 流程状态
     */
    private Integer processStatus;
    
    /**
     * 应用ID（来自流程实例）
     */
    private String appId;
}
