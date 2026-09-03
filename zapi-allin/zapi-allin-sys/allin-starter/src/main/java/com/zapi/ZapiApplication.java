package com.zapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ZAPI管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.zapi.**.mapper")
@EnableAsync
public class ZapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZapiApplication.class, args);

    }
} 