package com.zapi.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志事件类
 * 用于在系统模块中处理日志记录
 */
@Data
public class LogEvent {
    
    /**
     * 操作用户
     */
    private String username;
    
    /**
     * 操作用户ID
     */
    private Long userId;
    
    /**
     * 操作描述
     */
    private String operation;
    
    /**
     * 请求方法
     */
    private String method;
    
    /**
     * 请求参数
     */
    private String params;
    
    /**
     * 请求IP
     */
    private String ip;
    
    /**
     * 请求URL
     */
    private String url;
    
    /**
     * 请求方式
     */
    private String httpMethod;
    
    /**
     * 执行时长(毫秒)
     */
    private Long executionTime;
    
    /**
     * 操作结果 0-失败 1-成功
     */
    private Integer result;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    public LogEvent() {
        this.createTime = LocalDateTime.now();
    }
    
    public LogEvent(String username, Long userId, String operation, String method, String params, 
                   String ip, String url, String httpMethod, Long executionTime, 
                   Integer result, String errorMessage, String userAgent) {
        this.username = username;
        this.userId = userId;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.ip = ip;
        this.url = url;
        this.httpMethod = httpMethod;
        this.executionTime = executionTime;
        this.result = result;
        this.errorMessage = errorMessage;
        this.userAgent = userAgent;
        this.createTime = LocalDateTime.now();
    }
} 