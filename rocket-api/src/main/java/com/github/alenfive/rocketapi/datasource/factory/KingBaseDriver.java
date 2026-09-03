package com.github.alenfive.rocketapi.datasource.factory;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.KingBaseDataSource;
import com.github.alenfive.rocketapi.datasource.MySQLDataSource;
import com.github.alenfive.rocketapi.entity.DBConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KingBaseDriver extends JdbcDriver{

    @Override
    public String getName() {
        return "KingBase";
    }

    @Override
    public String getIcon() {
        return "rocketapi/images/KingBase.png";
    }

    @Override
    public String getFormat() {
        return "jdbc:kingbase8://localhost:54321/testdb";
    }

    @Override
    public DataSourceDialect factory(DBConfig config) throws Exception {
        return new KingBaseDataSource(super.getDataSource(config));
    }
}