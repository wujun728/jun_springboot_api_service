package com.ruoyi.api.aggregate.spi;

import javax.sql.DataSource;

public interface ReverseDataSourceProvider {

    DataSource getDataSource(String prefix);

    boolean containsPrefix(String prefix);
}