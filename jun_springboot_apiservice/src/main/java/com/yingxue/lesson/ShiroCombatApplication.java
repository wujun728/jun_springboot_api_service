package com.yingxue.lesson;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yingxue.lesson.mapper")
public class ShiroCombatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroCombatApplication.class, args);
    }

}
