package com.github.alenfive.rocketapi.datasource.factory;

import com.github.alenfive.rocketapi.entity.DBConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public abstract class JdbcDriver extends IDataSourceDialectDriver {

    protected PlatformTransactionManager getDataSource(DBConfig config){
        HikariConfig hikariConfig = new HikariConfig(config.getProperties());
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());

        DataSource dataSource = new HikariDataSource(hikariConfig);

        return new DataSourceTransactionManager(dataSource);
    }

}
