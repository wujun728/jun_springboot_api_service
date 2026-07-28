package com.ruoyi;

import com.ruoyi.api.mcp.annotation.EnableMcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@EnableMcpServer
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
public class RuoYiApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("start success !!!");
    }
}
