package com.zapi.form.annotation;


import com.zapi.enums.FormOperationEnum;

import java.lang.annotation.*;

/****
 * 定义数据库操作注解
 * @author zhonglz
 * @date 2025年08月21日14:56:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataBaseOperatePlus {

    /**
     * 数据库操作枚举
     */
    FormOperationEnum operationType();

    /**
     * 目标实体类型
     * 用于指定要匹配的参数类型
     */
    Class<?> targetEntityType();
}
