package com.github.alenfive.rocketapi.datasource;

import com.github.alenfive.rocketapi.entity.vo.Page;
import com.github.alenfive.rocketapi.entity.vo.TableInfo;
import com.github.alenfive.rocketapi.extend.IApiPager;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

/**
 * mysql 数据源
 */
public class ElasticSearchDataSource extends JdbcDataSource {

    private ElasticSearchDataSource(){}

    public ElasticSearchDataSource(EsDataSource dataSource) {
        super.dataSource = dataSource;
        super.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }


    @Override
    public String buildCountScript(String script, IApiPager apiPager, Page page) {
        return  "select count(1) from ("+script+") t1";
    }

    @Override
    public String buildPageScript(String script, IApiPager apiPager, Page page) {
        Integer offset = apiPager.getOffset(page.getPageSize(),page.getPageNo());
        return  script + " limit "+offset+","+page.getPageSize();
    }

    @Override
    public String transcoding(String param) {
        return param
                .replace("\\","\\\\")
                .replace("\"","\\\"")
                .replace("\'","\\\'");
    }

    @Override
    public List<TableInfo> buildTableInfo(){

        return Collections.emptyList();

    }
}
