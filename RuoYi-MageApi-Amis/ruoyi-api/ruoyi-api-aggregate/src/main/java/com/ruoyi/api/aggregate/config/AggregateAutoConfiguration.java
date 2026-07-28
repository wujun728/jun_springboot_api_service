package com.ruoyi.api.aggregate.config;

import com.ruoyi.api.aggregate.controller.ReverseApiController;
import com.ruoyi.api.aggregate.service.ReverseApiService;
import com.ruoyi.api.aggregate.service.ResultMerger;
import com.ruoyi.api.aggregate.spi.ReverseAuthProvider;
import com.ruoyi.api.aggregate.spi.ReverseDataSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

@Configuration
public class AggregateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReverseAuthProvider defaultAuthProvider() {
        return new ReverseAuthProvider() {
            @Override
            public boolean authenticate(Map<String, String> authHeaders) {
                return true;
            }

            @Override
            public String getType() {
                return "none";
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReverseDataSourceProvider defaultDataSourceProvider() {
        return new ReverseDataSourceProvider() {
            @Override
            public javax.sql.DataSource getDataSource(String prefix) {
                return null;
            }

            @Override
            public boolean containsPrefix(String prefix) {
                return false;
            }
        };
    }

    @Bean
    public ReverseApiService reverseApiService(ReverseAuthProvider authProvider, ReverseDataSourceProvider dataSourceProvider) {
        return new ReverseApiService(authProvider, dataSourceProvider);
    }

    @Bean
    public ResultMerger resultMerger() {
        return new ResultMerger();
    }

    @Bean
    public ReverseApiController reverseApiController(ReverseApiService reverseApiService, ResultMerger resultMerger) {
        return new ReverseApiController(reverseApiService, resultMerger);
    }
}