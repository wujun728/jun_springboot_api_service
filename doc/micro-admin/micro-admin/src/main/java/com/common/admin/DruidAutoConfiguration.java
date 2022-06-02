package com.common.admin;



import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @author ninghao
 *
 */

@Configuration
@EnableConfigurationProperties({DruidProperties.class})
public class DruidAutoConfiguration {

    @Autowired
    private DruidProperties properties;

    public DruidAutoConfiguration() {
    }
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(this.properties.getUrl());
        dataSource.setUsername(this.properties.getUsername());
        dataSource.setPassword(this.properties.getPassword());
        if(this.properties.getInitialSize().intValue() > 0) {
            dataSource.setInitialSize(this.properties.getInitialSize().intValue());
        }

        if(this.properties.getMinIdle().intValue() > 0) {
            dataSource.setMinIdle(this.properties.getMinIdle().intValue());
        }

        if(this.properties.getMaxActive().intValue() > 0) {
            dataSource.setMaxActive(this.properties.getMaxActive().intValue());
        }

        if(this.properties.getTestOnBorrow() != null) {
            dataSource.setTestOnBorrow(this.properties.getTestOnBorrow().booleanValue());
        }

        if(this.properties.getMaxWait().intValue() > 0) {
            dataSource.setMaxWait((long)this.properties.getMaxWait().intValue());
        }

        if(this.properties.getTimeBetweenEvictionRunsMillis().intValue() > 0) {
            dataSource.setTimeBetweenEvictionRunsMillis((long)this.properties.getTimeBetweenEvictionRunsMillis().intValue());
        }

        if(this.properties.getMinEvictableIdleTimeMillis().intValue() > 0) {
            dataSource.setMinEvictableIdleTimeMillis((long)this.properties.getMinEvictableIdleTimeMillis().intValue());
        }

        if(this.properties.getValidationQuery() != null) {
            dataSource.setValidationQuery(this.properties.getValidationQuery());
        }

        if(this.properties.getTestWhileIdle() != null) {
            dataSource.setTestWhileIdle(this.properties.getTestWhileIdle().booleanValue());
        }

        if(this.properties.getTestOnReturn() != null) {
            dataSource.setTestOnReturn(this.properties.getTestOnReturn().booleanValue());
        }

        if(this.properties.getPoolPreparedStatements() != null) {
            dataSource.setPoolPreparedStatements(this.properties.getPoolPreparedStatements().booleanValue());
        }

        if(this.properties.getMaxPoolPreparedStatementPerConnectionSize().intValue() > 0) {
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(this.properties.getMaxPoolPreparedStatementPerConnectionSize().intValue());
        }

        if(this.properties.getFilters() != null) {
            try {
                dataSource.setFilters(this.properties.getFilters());
            } catch (SQLException var4) {
                var4.printStackTrace();
            }
        }

        if(this.properties.getConnectionProperties() != null) {
            dataSource.setConnectionProperties(this.properties.getConnectionProperties());
        }

        try {
            dataSource.init();
            return dataSource;
        } catch (SQLException var3) {
            throw new RuntimeException(var3);
        }
    }    
}
