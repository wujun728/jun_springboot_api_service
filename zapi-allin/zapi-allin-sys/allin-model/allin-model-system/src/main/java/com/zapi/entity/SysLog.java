package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志实体类
 */
@Data
@TableName("sys_log")
public class SysLog {

    @TableId(type = IdType.AUTO)
    private Long id;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
} 