package com.zapi.workflow.service.common.exception;

/**
 * 工作流业务异常类
 * 用于处理工作流系统中的业务逻辑错误
 * 继承自RuntimeException，支持事务回滚
 */
public class WorkflowException extends RuntimeException {

    /**
     * 错误码
     * 用于标识具体的错误类型
     */
    private String errorCode;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public WorkflowException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public WorkflowException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public WorkflowException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误码
     *
     * @param errorCode 错误码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    // 常用的工作流异常静态方法

    /**
     * 流程定义不存在异常
     */
    public static WorkflowException processDefinitionNotFound(String processDefId) {
        return new WorkflowException("PROCESS_DEF_NOT_FOUND", 
            "流程定义不存在: " + processDefId);
    }

    /**
     * 流程实例不存在异常
     */
    public static WorkflowException processInstanceNotFound(String instanceId) {
        return new WorkflowException("PROCESS_INSTANCE_NOT_FOUND", 
            "流程实例不存在: " + instanceId);
    }

    /**
     * 任务不存在异常
     */
    public static WorkflowException taskNotFound(String taskId) {
        return new WorkflowException("TASK_NOT_FOUND", 
            "任务不存在: " + taskId);
    }

    /**
     * 任务状态错误异常
     */
    public static WorkflowException invalidTaskStatus(String taskId, Integer currentStatus) {
        return new WorkflowException("INVALID_TASK_STATUS", 
            "任务状态错误，无法处理。任务ID: " + taskId + "，当前状态: " + currentStatus);
    }

    /**
     * 流程定义格式错误异常
     */
    public static WorkflowException invalidProcessDefinition(String reason) {
        return new WorkflowException("INVALID_PROCESS_DEFINITION", 
            "流程定义格式错误: " + reason);
    }

    /**
     * 节点不存在异常
     */
    public static WorkflowException nodeNotFound(String nodeId) {
        return new WorkflowException("NODE_NOT_FOUND", 
            "节点不存在: " + nodeId);
    }

    /**
     * 条件表达式错误异常
     */
    public static WorkflowException expressionError(String expression, String reason) {
        return new WorkflowException("EXPRESSION_ERROR", 
            "条件表达式错误: " + expression + "，原因: " + reason);
    }
}
