package com.ruoyi.api.groovy.config;

import com.ruoyi.api.groovy.entity.DBConfig;
import com.ruoyi.api.groovy.engine.SqlApiEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DBConfig.class)
public class SqlEngineConfiguration {

    private final DBConfig dbConfig;

    public SqlEngineConfiguration(DBConfig config) {
        this.dbConfig = config;
    }

    @Bean
    @ConditionalOnMissingBean(SqlApiEngine.class)
    public SqlApiEngine Engine() {
        return new SqlApiEngine(dbConfig);
    }
}