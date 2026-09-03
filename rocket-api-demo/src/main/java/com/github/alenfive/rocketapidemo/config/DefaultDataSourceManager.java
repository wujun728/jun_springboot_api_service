package com.github.alenfive.rocketapidemo.config;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.DataSourceManager;
import com.github.alenfive.rocketapi.datasource.MySQLDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认数据源管理器
 */
@Component
public class DefaultDataSourceManager extends DataSourceManager {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PostConstruct
    public void init() {
        Map<String, DataSourceDialect> dialects = new HashMap<>();
        dialects.put("mysql", new MySQLDataSource(transactionManager,true));
        super.setDialectMap(dialects);
    }
}
