package org.ssssssss.script.annotation;


import java.lang.annotation.*;

/**
 * 标识是个函数
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Function {
}
