package com.yatop.lambda.api.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.loader.Loader;
import com.yuyaogc.lowcode.engine.loader.SdkLoader;
import com.yuyaogc.lowcode.engine.plugin.Plugins;
import com.yuyaogc.lowcode.engine.plugin.activerecord.ActiveRecordPlugin;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class EngineConfig {
    final static Plugins me = new Plugins();

    @Bean
    @ConfigurationProperties(prefix = "lambda-portal.db-datasource")
    public DataSource xaDataSource() throws Exception {
        return new DruidDataSource();
    }

    @Bean
    public Loader run(DataSource dataSource) throws Exception {
        ActiveRecordPlugin arp = new ActiveRecordPlugin("master", dataSource);
        me.add(arp);

        Loader.setLoader(new SdkLoader());
        Loader.getLoader().loadPlugin(me);
        Loader.getLoader().up();

        return Loader.getLoader();
    }


}
