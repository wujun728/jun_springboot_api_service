package com.ruoyi.common.cloud.annotation;

import java.lang.annotation.*;

/**
 * 微服务模式启动注解
 * 继承AutoWeb的所有功能，并添加Feign客户端支持
 *
 * @author ruoyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoFeignClients
@AutoWeb
public @interface AutoCloud {
}
