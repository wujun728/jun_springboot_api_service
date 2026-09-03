package com.yuyaogc.lowcode.engine.plugin.druid;

import javax.sql.DataSource;


public interface IDataSourceProvider {
    DataSource getDataSource();
}
