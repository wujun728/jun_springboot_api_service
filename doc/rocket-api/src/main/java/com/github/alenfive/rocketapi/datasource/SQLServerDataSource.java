package com.github.alenfive.rocketapi.datasource;

import com.github.alenfive.rocketapi.entity.vo.Page;
import com.github.alenfive.rocketapi.entity.vo.TableInfo;
import com.github.alenfive.rocketapi.extend.IApiPager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql server 数据源
 */
public class SQLServerDataSource extends JdbcDataSource {

    Pattern pattern = Pattern.compile("(order +by .*)",Pattern.CASE_INSENSITIVE);

    public SQLServerDataSource() {
    }

    public SQLServerDataSource(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    public SQLServerDataSource(PlatformTransactionManager transactionManager, boolean storeApi) {
        super(transactionManager, storeApi);
    }

    @Override
    public String buildCountScript(String script, IApiPager apiPager, Page page) {
        Matcher matcher = pattern.matcher(script);
        script = matcher.replaceAll("");
        return "select count(1) from ("+script+") t1";
    }

    @Override
    public String buildPageScript(String script, IApiPager apiPager, Page page) {
        Integer offset = apiPager.getOffset(page.getPageSize(),page.getPageNo());
        return script + " OFFSET "+offset+" ROWS FETCH NEXT "+page.getPageSize()+" ROWS ONLY";
    }

    @Override
    public String transcoding(String param) {
        return param
                .replace("'","''");
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }


}
