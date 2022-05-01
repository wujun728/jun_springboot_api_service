package org.ssssssss.script.annotation;

import java.lang.annotation.*;

/**
 * 方法、字段、参数注释
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comment {

	String value();

	String name() default "";

	/**
	 * 是否返回原类型
	 */
	boolean origin() default false;
}
