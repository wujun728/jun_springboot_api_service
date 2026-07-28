package com.ruoyi.api.openapi.config;

import com.ruoyi.api.openapi.auth.AppKeyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class OpenApiAutoConfiguration {

    @Bean
    public AppKeyManager appKeyManager() {
        return new AppKeyManager();
    }
}