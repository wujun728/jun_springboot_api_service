package com.gitee.freakchicken;

import com.gitee.freakchicken.entity.DBConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DBConfig.class)
public class DBApiConfiguration {

    private final DBConfig dbConfig;

    public DBApiConfiguration(DBConfig config) {
        this.dbConfig = config;
    }

    @Bean
    @ConditionalOnMissingBean(DBAPI.class)
    public DBAPI Engine() {
        return new DBAPI(dbConfig);
    }
}