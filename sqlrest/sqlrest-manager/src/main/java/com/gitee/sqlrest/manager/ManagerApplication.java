package com.gitee.sqlrest.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@EnableDiscoveryClient
@MapperScan("com.gitee.sqlrest.persistence.mapper")
@SpringBootApplication(
    scanBasePackages = {
        "com.gitee.sqlrest.persistence",
        "com.gitee.sqlrest.core.driver",
        "com.gitee.sqlrest.core.gateway",
        "com.gitee.sqlrest.core.service",
        "com.gitee.sqlrest.core.exec",
        "com.gitee.sqlrest.manager"
    }
)
public class ManagerApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ManagerApplication.class);
    springApplication.setBannerMode(Banner.Mode.OFF);
    springApplication.run(args);
  }
}
