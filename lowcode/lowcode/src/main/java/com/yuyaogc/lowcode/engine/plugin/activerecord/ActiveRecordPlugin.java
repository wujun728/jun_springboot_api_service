package com.yuyaogc.lowcode.engine.plugin.activerecord;


import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.druid.IDataSourceProvider;

import javax.sql.DataSource;

public class ActiveRecordPlugin implements IPlugin {
    protected IDataSourceProvider dataSourceProvider = null;
    protected Boolean devMode = null;
    protected Config config = null;
    protected volatile boolean isStarted = false;


    public ActiveRecordPlugin(IDataSourceProvider dataSourceProvider) {
        this(Db.MAIN_CONFIG_NAME, dataSourceProvider);
    }


    public ActiveRecordPlugin(String name, IDataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
        config = new Config(name, dataSourceProvider.getDataSource(), new MySqlDialect());
    }

    public ActiveRecordPlugin(String name, DataSource dataSource) {
        config = new Config(name, dataSource, new MySqlDialect());
    }

    public Boolean getDevMode() {
        return devMode;
    }

    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    @Override
    public boolean start() {
        if (isStarted) {
            return true;
        }
        if (config.dataSource == null && dataSourceProvider != null) {
            config.dataSource = dataSourceProvider.getDataSource();
        }
        if (config.dataSource == null) {
            throw new RuntimeException("relation start error: RelationPlugin need DataSource or DataSourceProvider");
        }
        Db.addDb(config);
        isStarted = true;
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
