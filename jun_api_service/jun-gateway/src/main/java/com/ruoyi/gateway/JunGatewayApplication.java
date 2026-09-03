package com.ruoyi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JunGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(JunGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Jun Gateway 启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
