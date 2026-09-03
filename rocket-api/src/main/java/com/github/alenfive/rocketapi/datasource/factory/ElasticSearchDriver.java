package com.github.alenfive.rocketapi.datasource.factory;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.ElasticSearchDataSource;
import com.github.alenfive.rocketapi.entity.DBConfig;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * SQL  构造器
 */
@Component
public class ElasticSearchDriver extends JdbcDriver {

    @Override
    public String getName() {
        return "ElasticSearch";
    }

    @Override
    public String getIcon() {
        return "rocketapi/images/ElasticSearch.png";
    }

    @Override
    public String getFormat() {
        return "jdbc:es://localhost:9300/";
    }

    @Override
    public DataSourceDialect factory(DBConfig config) throws Exception {
        Properties properties = new Properties();
        properties.putAll(config.getProperties());

        if (StringUtils.hasText(config.getUser())){
            properties.put("user",config.getUser());
        }
        if (StringUtils.hasText(config.getPassword())){
            properties.put("password",config.getPassword());
        }
        EsDataSource dataSource = new EsDataSource();
        String address = "jdbc:es://localhost:9200";
        dataSource.setUrl(address);
        dataSource.setProperties(properties);

        return new ElasticSearchDataSource(dataSource);
    }
}
