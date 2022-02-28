package com.jun.plugin;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("com.jun.plugin.**.mapper")
@Slf4j
//@ServletComponentScan(basePackages = {"com.cosmoplat.config.filter"}) //这一句完成了配置，Springboot的”懒理念“真的厉害。
public class ApiServiceApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(ApiServiceApplication.class, args);

        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Api Test Json: \thttp://{}:{}/public/json\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

}
