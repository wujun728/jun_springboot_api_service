package com.gitee.freakchicken.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: dbapi-starter
 * @description:
 * @author: jiangqiang
 * @create: 2021-03-11 10:49
 **/

@ConfigurationProperties("dbapi.config")
public class DBConfig {
    String sql;

    String datasource;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }
}
