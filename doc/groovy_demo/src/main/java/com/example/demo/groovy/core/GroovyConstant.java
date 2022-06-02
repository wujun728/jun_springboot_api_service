/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.example.demo.groovy.core;

/**
 * Groovy 相关常量
 */
public final class GroovyConstant {

    /**
     * GROOVY 脚本配置标签
     */
    public static final String SPRING_TAG = "lang:groovy";

    /**
     * GROOVY 脚本在配置中的前缀
     */
    public static final String SCRIPT_SOURCE_PREFIX = "database:";


    /**
     * 配置文件开头
     */
    public static final String BEANS_FILE_CONTENT = "<beans xmlns='http://www.springframework.org/schema/beans'\n"
            + "    xmlns:lang='http://www.springframework.org/schema/lang'\n"
            + "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "    xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd\n"
            + "                        http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang-2.5.xsd' default-autowire='byName'>"
            + "</beans>\n";

    /**
     * 禁用构造函数
     */
    private GroovyConstant() {
        // 禁用构造函数
    }
}
