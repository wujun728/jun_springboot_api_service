package com.github.alenfive.rocketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RocketAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketAPIApplication.class, args);
    }
}
