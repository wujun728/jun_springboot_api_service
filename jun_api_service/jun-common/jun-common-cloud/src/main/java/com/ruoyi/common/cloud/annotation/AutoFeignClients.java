package com.ruoyi.common.cloud.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * 自定义Feign客户端注解
 * 扫描 com.ruoyi 下所有Feign接口
 *
 * @author ruoyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(basePackages = {"com.ruoyi"})
public @interface AutoFeignClients {
}
