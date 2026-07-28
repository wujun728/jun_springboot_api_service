package com.ruoyi.api.aggregate.service;

import com.ruoyi.api.aggregate.helper.DBHelper;
import com.ruoyi.api.aggregate.spi.ReverseAuthProvider;
import com.ruoyi.api.aggregate.spi.ReverseDataSourceProvider;

import java.util.*;

public class ReverseApiService {

    private final ReverseAuthProvider authProvider;
    private final ReverseDataSourceProvider dataSourceProvider;

    public ReverseApiService(ReverseAuthProvider authProvider, ReverseDataSourceProvider dataSourceProvider) {
        this.authProvider = authProvider;
        this.dataSourceProvider = dataSourceProvider;
    }

    public Object execute(String prefix, String name, Map<String, Object> params, Map<String, String> authHeaders) {
        if (!authProvider.authenticate(authHeaders)) {
            throw new SecurityException("Authentication failed");
        }

        javax.sql.DataSource ds = dataSourceProvider.getDataSource(prefix);
        if (ds == null) {
            throw new IllegalArgumentException("DataSource not found for prefix: " + prefix);
        }

        DBHelper dbHelper = new DBHelper(ds);
        List<Map<String, Object>> resultList = dbHelper.executeSql(buildSql(name, params));

        return transformResult(resultList, params);
    }

    private String buildSql(String name, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(name);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            int idx = sql.indexOf(placeholder);
            if (idx != -1) {
                sql.replace(idx, idx + placeholder.length(), "'" + entry.getValue() + "'");
            }
        }
        return sql.toString();
    }

    private Object transformResult(List<Map<String, Object>> resultList, Map<String, Object> params) {
        if (resultList.size() == 1) {
            return resultList.get(0);
        }
        return resultList;
    }
}