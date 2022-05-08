package com.github.alenfive.rocketapi.config;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.DataSourceManager;
import com.github.alenfive.rocketapi.datasource.MongoDataSource;
import com.github.alenfive.rocketapi.datasource.MySQLDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认数据源管理器
 */
@Component
@Slf4j
public class DefaultDataSourceManager extends DataSourceManager {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        log.warn("\n--------------------------------------\n" +
                " !!! 不要启动我，我只为编译调试源码而生，调试成功后mvn package 会输出 'rocket-api-boot-starter',关于如何使用stater,请看业务集成demo:https://gitee.com/alenfive/rocket-api-demo" +
                "\n--------------------------------------\n");
        Map<String,DataSourceDialect> dialectMap = new LinkedHashMap<>();
        dialectMap.put("mysql",new MySQLDataSource(transactionManager,true));
        dialectMap.put("mongodb",new MongoDataSource(mongoTemplate));
        super.setDialectMap(dialectMap);
    }
}
