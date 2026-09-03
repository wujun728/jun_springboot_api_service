package com.zapi.openapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用认证注解
 * 用于需要应用认证的接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAppAuth {
    /**
     * 是否必须认证，默认为true
     */
    boolean required() default true;
    
    /**
     * 认证失败时的错误消息
     */
    String message() default "应用认证失败";
}