package com.zapi.annotation;

import java.lang.annotation.*;

/**
 * 日志操作注解
 * 用于标记需要记录日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    
    /**
     * 操作描述
     */
    String value() default "";
    
    /**
     * 是否记录请求参数
     */
    boolean logParams() default true;
    
    /**
     * 是否记录响应结果
     */
    boolean logResult() default false;
    
    /**
     * 是否记录执行时间
     */
    boolean logExecutionTime() default true;
} 