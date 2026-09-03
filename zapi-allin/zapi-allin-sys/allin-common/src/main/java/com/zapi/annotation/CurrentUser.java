package com.zapi.annotation;

import java.lang.annotation.*;

/**
 * 当前用户注解
 * 用于在Controller方法参数中注入当前登录用户ID
 * 
 * 使用示例：
 * <pre>
 * // 注入Long类型用户ID
 * public Result doSomething(@CurrentUser Long userId) { ... }
 * 
 * // 注入String类型用户ID
 * public Result doSomething(@CurrentUser String userId) { ... }
 * 
 * // 必须登录，未登录抛出异常
 * public Result doSomething(@CurrentUser(required = true) Long userId) { ... }
 * </pre>
 * 
 * @author zapi
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    
    /**
     * 是否必须登录
     * true: 未登录时抛出异常
     * false: 未登录时返回null
     */
    boolean required() default false;
}
