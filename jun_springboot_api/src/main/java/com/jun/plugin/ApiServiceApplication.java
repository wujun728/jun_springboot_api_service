package com.jun.plugin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.jun.plugin.api.mapper")
public class ApiServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiServiceApplication.class, args);
	}
}
