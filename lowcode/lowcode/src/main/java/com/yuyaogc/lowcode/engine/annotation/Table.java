package com.yuyaogc.lowcode.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";

    String displayName() default "";

    String catalog() default "";

    String schema() default "";

    String[] parent() default {};

    boolean isAbstract() default false;

    UniqueConstraint[] uniqueConstraints() default {};
}
