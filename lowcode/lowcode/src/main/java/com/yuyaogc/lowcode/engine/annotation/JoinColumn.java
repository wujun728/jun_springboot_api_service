package com.yuyaogc.lowcode.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumn {
    String name() default "";

    String referencedColumnName() default "id";

    boolean unique() default false;

    boolean nullable() default true;

}
