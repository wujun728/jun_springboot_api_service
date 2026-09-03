package com.ruoyi.common.cloud.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.*;

/**
 * 单体模式启动注解
 * 用于单体模式下的应用启动，包含SpringBootApplication和MyBatis扫描
 *
 * @author ruoyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootApplication(scanBasePackages = {"com.ruoyi"})
@MapperScan({"com.ruoyi.**.mapper"})
public @interface AutoWeb {
}
