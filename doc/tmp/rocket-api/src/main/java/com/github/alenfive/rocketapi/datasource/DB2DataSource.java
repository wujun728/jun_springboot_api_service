package com.github.alenfive.rocketapi.datasource;

import com.github.alenfive.rocketapi.entity.vo.Page;
import com.github.alenfive.rocketapi.entity.vo.TableInfo;
import com.github.alenfive.rocketapi.extend.IApiPager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * db2 数据源
 */
public class DB2DataSource extends JdbcDataSource {

    public DB2DataSource() {
    }

    public DB2DataSource(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    public DB2DataSource(PlatformTransactionManager transactionManager, boolean storeApi) {
       super(transactionManager,storeApi);
    }

    @Override
    public String buildCountScript(String script, IApiPager apiPager, Page page) {
        return "select count(1) from ("+script+") t1";
    }

    @Override
    public String buildPageScript(String script, IApiPager apiPager, Page page) {
        Integer offset = apiPager.getOffset(page.getPageSize(),page.getPageNo());
        return "SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( " + script +
                " ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN "+(offset+1)+" AND "+(offset + page.getPageSize());
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }
}
