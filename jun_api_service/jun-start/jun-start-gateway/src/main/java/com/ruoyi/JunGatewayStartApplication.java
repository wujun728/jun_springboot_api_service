package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关微服务启动类
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JunGatewayStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(JunGatewayStartApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Jun Gateway 微服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
