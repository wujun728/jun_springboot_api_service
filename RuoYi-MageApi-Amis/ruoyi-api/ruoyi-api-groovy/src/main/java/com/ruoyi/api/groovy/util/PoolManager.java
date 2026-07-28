package com.ruoyi.api.groovy.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ruoyi.api.groovy.entity.ApiDataSource;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class PoolManager {

    private static ConcurrentHashMap<String, DruidDataSource> map = new ConcurrentHashMap<>();

    private static DruidDataSource getJdbcConnectionPool(ApiDataSource ds) {
        if (map.containsKey(ds.getId())) {
            return map.get(ds.getId());
        } else {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(ds.getUrl());
            druidDataSource.setUsername(ds.getUsername());
            druidDataSource.setDriverClassName(ds.getDriver());
            druidDataSource.setConnectionErrorRetryAttempts(3);
            druidDataSource.setBreakAfterAcquireFailure(true);
            druidDataSource.setPassword(ds.getPassword());
            druidDataSource.setName(ds.getId());
            map.put(ds.getId(), druidDataSource);
            return map.get(ds.getId());
        }
    }

    public static DruidPooledConnection getPooledConnection(ApiDataSource ds) throws SQLException {
        DruidDataSource pool = PoolManager.getJdbcConnectionPool(ds);
        DruidPooledConnection connection = pool.getConnection();
        return connection;
    }
}