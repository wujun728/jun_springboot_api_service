package com.github.alenfive.rocketapi.config;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.DataSourceManager;
import com.github.alenfive.rocketapi.entity.DBConfig;
import com.github.alenfive.rocketapi.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * 数据源加载检查重试
 */
@Configuration
@ConditionalOnProperty(value = "spring.rocket-api.datasource.check.enabled",havingValue = "true",matchIfMissing = true)
@Slf4j
public class DataSourceCheckConfig {

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private DataSourceService dataSourceService;

    @Scheduled(initialDelayString = "${spring.rocket-api.datasource.check.fixed-delay:PT1M}",fixedDelayString = "${spring.rocket-api.datasource.check.fixed-delay:PT1M}")
    public void validate(){
        List<DBConfig> dbConfigList = dataSourceService.getDBConfig();
        Map<String, DataSourceDialect> dialectMap = dataSourceManager.getDialectMap();
        for (DBConfig config : dbConfigList){
            DataSourceDialect dialect = dialectMap.get(config.getName());
            if (dialect == null){
                try {
                    dataSourceService.loadDBConfig(config);
                } catch (Exception e) {
                    log.error("load db error:{}",config);
                }
            }
        }
    }
}
