package com.yuyaogc.lowcode.engine.annotation;

import com.yuyaogc.lowcode.engine.enums.IdType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    String value() default "";

    IdType type() default IdType.NONE;
}
