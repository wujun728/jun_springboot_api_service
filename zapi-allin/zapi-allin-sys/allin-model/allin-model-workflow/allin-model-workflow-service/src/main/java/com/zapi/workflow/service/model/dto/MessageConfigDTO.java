package com.zapi.workflow.service.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * 节点消息配置DTO
 * 支持两种结构：
 * 1. 审批节点：四种发送时机的独立消息配置（onPending/onApprove/onReject/onComplete）
 * 2. 抄送节点：简化结构（enabled/title/content）
 */
@Data
public class MessageConfigDTO {
    
    // ========== 抄送节点简化字段 ==========
    
    /**
     * 是否启用（抄送节点使用）
     */
    private Boolean enabled;
    
    /**
     * 消息标题（抄送节点使用）
     */
    private String title;
    
    /**
     * 消息内容（抄送节点使用）
     */
    private String content;
    
    // ========== 审批节点多时机字段 ==========
    
    /**
     * 待审批通知配置（通知审批人有待办任务）
     */
    private MessageTimingConfig onPending;
    
    /**
     * 审批通过通知配置（通知发起人审批已通过）
     */
    private MessageTimingConfig onApprove;
    
    /**
     * 审批驳回通知配置（通知发起人审批被驳回）
     */
    private MessageTimingConfig onReject;
    
    /**
     * 审批完成通知配置（通知发起人审批已完成）
     */
    private MessageTimingConfig onComplete;
    
    /**
     * 消息参数配置（所有时机共享）
     * key: 参数名（如 flowName）
     * value: 参数配置（来源类型和值）
     */
    private Map<String, ParamConfig> params;
    
    /**
     * 单个发送时机的消息配置
     */
    @Data
    public static class MessageTimingConfig {
        /**
         * 是否启用此时机的消息发送
         */
        private boolean enabled;
        
        /**
         * 消息标题，支持 ${xxx} 变量
         */
        private String title;
        
        /**
         * 消息内容，支持 ${xxx} 变量
         */
        private String content;
    }
    
    /**
     * 参数配置
     */
    @Data
    public static class ParamConfig {
        /**
         * 参数来源类型
         * FIXED: 固定值
         * VARIABLE: 流程变量
         * FORM_FIELD: 表单字段
         */
        private String sourceType;
        
        /**
         * 参数值（根据sourceType含义不同）
         * FIXED: 直接值
         * VARIABLE: 变量名
         * FORM_FIELD: 字段名
         */
        private String value;
    }
    
    /**
     * 判断是否有任何时机启用了消息发送
     */
    public boolean hasAnyEnabled() {
        return (onPending != null && onPending.isEnabled()) ||
               (onApprove != null && onApprove.isEnabled()) ||
               (onReject != null && onReject.isEnabled()) ||
               (onComplete != null && onComplete.isEnabled());
    }
    
    /**
     * 获取指定时机的配置
     */
    public MessageTimingConfig getTimingConfig(String timing) {
        switch (timing) {
            case "onPending":
                return onPending;
            case "onApprove":
                return onApprove;
            case "onReject":
                return onReject;
            case "onComplete":
                return onComplete;
            default:
                return null;
        }
    }
}
