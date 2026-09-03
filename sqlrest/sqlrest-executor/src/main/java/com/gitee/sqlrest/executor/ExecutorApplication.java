package com.gitee.sqlrest.executor;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@MapperScan("com.gitee.sqlrest.persistence.mapper")
@SpringBootApplication(
    scanBasePackages = {
        "com.gitee.sqlrest.persistence",
        "com.gitee.sqlrest.core.filter",
        "com.gitee.sqlrest.core.driver",
        "com.gitee.sqlrest.core.servlet",
        "com.gitee.sqlrest.core.exec",
        "com.gitee.sqlrest.cache",
        "com.gitee.sqlrest.executor",
    }
)
public class ExecutorApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ExecutorApplication.class);
    springApplication.setBannerMode(Banner.Mode.OFF);
    springApplication.run(args);
  }
}
