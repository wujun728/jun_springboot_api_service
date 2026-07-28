package com.ruoyi.api.core.spi;

import javax.sql.DataSource;
import java.util.Map;

public interface DataSourceProvider {

    String getType();

    DataSource createDataSource(String url, String username, String password, String driverClassName, Map<String, Object> properties);

    boolean testConnection(String url, String username, String password, String driverClassName);

    void destroy(DataSource dataSource);

}