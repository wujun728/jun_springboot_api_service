package com.ruoyi.api.version.config;

import com.ruoyi.api.version.service.VersionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionAutoConfiguration {

    @Bean
    public VersionService versionService() {
        return new VersionService();
    }
}