package com.ruoyi.api.plugin.config;

import com.ruoyi.api.plugin.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class PluginAutoConfiguration {

    @PostConstruct
    public void init() {
        PluginManager.loadPlugins();
    }

    @Bean
    public PluginManager pluginManager() {
        return new PluginManager();
    }
}