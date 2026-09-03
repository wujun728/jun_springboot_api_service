package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.util.Map;

/**
 * 完成任务请求DTO
 * 用于任务审批时传递的参数信息
 */
@Data
public class CompleteTaskDTO {
    
    /**
     * 任务ID
     * 必填字段，对应wf_task.id
     * 标识要完成的具体任务
     */
    private String taskId;
    
    /**
     * 操作人
     * 必填字段，实际处理任务的用户ID或用户名
     * 可能与原assignee不同（代理处理等情况）
     */
    private String operator;
    
    /**
     * 审批意见
     * 可选字段，任务处理时的备注说明
     * 记录审批人的决策依据，最大长度512字符
     */
    private String comment;
    
    /**
     * 是否审批通过
     * 默认值：true（通过）
     * true：审批通过，流程继续执行
     * false：审批驳回，流程结束或退回
     */
    private boolean approved = true;
    
    /**
     * 流程变量
     * 可选字段，任务完成时可以更新的流程变量
     * 键值对形式，例如：{"nextApprover": "张三", "amount": 5000}
     * 这些变量会合并到流程实例的variables中
     */
    private Map<String, Object> variables;
}
    