package com.yuyaogc.lowcode.engine.annotation;

import com.yuyaogc.lowcode.engine.util.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {

    String targetModel() default "";


    String targetProperty() default "";


    FetchType fetch() default FetchType.LAZY;


    String mappedBy() default "";
}
