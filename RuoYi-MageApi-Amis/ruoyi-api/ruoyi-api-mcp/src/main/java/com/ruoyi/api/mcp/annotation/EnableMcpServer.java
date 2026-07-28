package com.ruoyi.api.mcp.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMcpServer {
    String name() default "RuoYi-MCP";
    String version() default "1.0";
}