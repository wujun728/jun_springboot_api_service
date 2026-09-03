package com.github.alenfive.rocketapiplatform.rocketapiplatform;

import com.github.alenfive.rocketapi.datasource.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认数据源管理器
 */
@Component
public class DefaultDataSourceManager extends DataSourceManager {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        Map<String, DataSourceDialect> dialects = new HashMap<>();
        dialects.put("mysql", new MySQLDataSource(jdbcTemplate, true));
        //dialects.put("sqlserver", new SQLServerDataSource(jdbcTemplate, true));
        //dialects.put("postgres", new PostgreSQLDataSource(jdbcTemplate, true));
        super.setDialectMap(dialects);
    }
}
