package com.ruoyi;

import com.ruoyi.common.cloud.annotation.AutoCloud;
import org.springframework.boot.SpringApplication;

/**
 * 系统模块微服务启动类
 * 使用@AutoCloud注解启用Spring Cloud组件（Nacos、Feign等）
 *
 * @author ruoyi
 */
@AutoCloud
public class JunSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(JunSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Jun System 微服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
