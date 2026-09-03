package com.ruoyi;

import com.ruoyi.common.cloud.annotation.AutoWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 单体模式启动类
 * 将所有模块打包为一个应用，不使用Spring Cloud组件
 * 功能等同于原 ruoyi-admin 的 RuoYiApplication
 *
 * @author ruoyi
 */
@AutoWeb
public class JunBootApplication {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.nacos.discovery.enabled", "false");
        System.setProperty("spring.cloud.nacos.config.enabled", "false");
        SpringApplication.run(JunBootApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Jun Boot 单体模式启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
