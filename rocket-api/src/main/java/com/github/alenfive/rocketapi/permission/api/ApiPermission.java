package com.github.alenfive.rocketapi.permission.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bxf
 * @version 1.0
 * @date 2023/11/10 10:51
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPermission {

    boolean required() default true;

    String[] permissions() default {};
}
