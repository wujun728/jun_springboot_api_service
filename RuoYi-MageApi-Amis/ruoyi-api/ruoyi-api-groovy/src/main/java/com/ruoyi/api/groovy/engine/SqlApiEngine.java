package com.ruoyi.api.groovy.engine;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ruoyi.api.groovy.entity.ApiDataSource;
import com.ruoyi.api.groovy.entity.ApiSql;
import com.ruoyi.api.groovy.entity.DBConfig;
import com.ruoyi.api.groovy.entity.GroovyPage;
import com.ruoyi.api.groovy.util.JdbcUtil;
import com.ruoyi.api.groovy.util.PoolManager;
import com.ruoyi.api.groovy.util.XmlParser;
// TODO: Replace DynamicSqlEngine with RuoYi's SQL parsing or inline replacement
//import io.github.wujun728.sql.engine.DynamicSqlEngine;
//import io.github.wujun728.sql.SqlMeta;
//import io.github.wujun728.sql.entity.SqlWithParam;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SqlApiEngine {

    private static Logger logger = LoggerFactory.getLogger(SqlApiEngine.class);

    // TODO: Replace with compatible SQL parsing
    // DynamicSqlEngine dynamicSqlEngine = new DynamicSqlEngine();

    DBConfig dbConfig;

    Map<String, Map<String, ApiSql>> sqlMap = new HashMap<>();
    Map<String, ApiDataSource> dataSourceMap;

    public SqlApiEngine(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
        initXml();
    }

    public void initXml() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(this.dbConfig.getSql());

            for (Resource res : resources) {
                String filename = res.getFilename();
                if(filename.equals("ds.xml")){
                    continue;
                }
                InputStream inputStream = res.getInputStream();
                String content = IOUtils.toString(inputStream, Charsets.toCharset(StandardCharsets.UTF_8));
                Map<String, ApiSql> stringSqlMap = XmlParser.parseSql(content);
                this.sqlMap.put(filename.split("\\.")[0], stringSqlMap);
                logger.info("APISQL register sql xml: {}", filename);
            }
            Resource dsResource = resolver.getResource(this.dbConfig.getDatasource());
            String filename = dsResource.getFilename();
            InputStream inputStream = dsResource.getInputStream();
            String content = IOUtils.toString(inputStream, Charsets.toCharset(StandardCharsets.UTF_8));
            this.dataSourceMap = XmlParser.parseDatasource(content);
            logger.info("APISQL register datasource xml: {}", filename);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }


    public GroovyPage<JSONObject> executeQueryPage(String namespace, String sqlId, Map<String, Object> data, int pageNumber, int limit) {
        GroovyPage pageVo = new GroovyPage();
        ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
        if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
            throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
        }
        data.put("start",(pageNumber-1)*(limit));
        data.put("end",pageNumber*limit);
        ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
        String pageSql = sql.getText()+ " LIMIT #{start}, #{end} ";
        // TODO: Replace DynamicSqlEngine.parse() with inline replacement
        // SqlMeta sqlMeta = dynamicSqlEngine.parse(pageSql, data);
        // List<JSONObject> results = SqlApiEngine.executeQuery(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
        List<JSONObject> results = null;

        String totalSql = "  select count(1) as count FROM ( "+sql.getText()+" ) AS subquery ";
        // SqlMeta sqlMeta2 = dynamicSqlEngine.parse(totalSql, data);

        // Long totalpage = SqlApiEngine.executeQueryInt(dataSource, sqlMeta2.getSql(), sqlMeta2.getJdbcParamValues());
        Long totalpage = 0L;
        pageVo.setList(results);
        pageVo.setTotalRow(Math.toIntExact(totalpage));
        pageVo.setPageNumber(pageNumber);
        pageVo.setPageSize(limit);
        pageVo.setTotalPage(Math.toIntExact(totalpage) / limit);
        return pageVo;
    }

    public <T> List<T> executeQueryEntity(String namespace, String sqlId, Map<String, Object> data, Class<T> clazz) {
        List<JSONObject> list = executeQuery(namespace, sqlId, data);

        List<T> collect = list.stream().map(t -> t.toBean(clazz)).collect(Collectors.toList());
        return collect;
    }

    public List<JSONObject> executeQuery(String namespace, String sqlId, Map<String, Object> data) {
        try {
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                if (!sqlMap.get(namespace).containsKey(sqlId)) {
                    throw new RuntimeException("sqlId not found : " + sqlId);
                } else {
                    ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                    if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                        throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                    }
                    ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                    // TODO: Replace DynamicSqlEngine.parse()
                    // SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), data);
                    // return SqlApiEngine.executeQuery(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public JSONObject executeQueryOne(String namespace, String sqlId, Map<String, Object> data) {
        try {
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                if (!sqlMap.get(namespace).containsKey(sqlId)) {
                    throw new RuntimeException("sqlId not found : " + sqlId);
                } else {
                    ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                    if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                        throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                    }
                    ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                    // TODO: Replace DynamicSqlEngine.parse()
                    // SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), data);
                    // return SqlApiEngine.executeQueryById(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public int executeQueryCount(String namespace, String sqlId, Map<String, Object> data) {
        try {
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                if (!sqlMap.get(namespace).containsKey(sqlId)) {
                    throw new RuntimeException("sqlId not found : " + sqlId);
                } else {
                    ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                    if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                        throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                    }
                    ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                    // TODO: Replace DynamicSqlEngine.parse()
                    // SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), data);
                    // Object obj = SqlApiEngine.executeQueryOneColumn(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                    Object obj = null;
                    if(obj instanceof Integer){
                        return (int) obj;
                    }else {
                        return (Integer) obj;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<JSONObject> executeQuery(String namespace, String sqlId) {
        return executeQuery(namespace, sqlId, null);
    }

    public <T> List<T> executeQueryEntity(String namespace, String sqlId, Class<T> clazz) {
        return executeQueryEntity(namespace, sqlId, null, clazz);
    }

    public int executeUpdate(String namespace, String sqlId, Map<String, Object> data) {
        try {
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                if (!sqlMap.get(namespace).containsKey(sqlId)) {
                    throw new RuntimeException("sqlId not found : " + sqlId);
                } else {
                    ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                    if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                        throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                    }
                    ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                    // TODO: Replace DynamicSqlEngine.parse()
                    // SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), data);
                    // return SqlApiEngine.executeUpdate(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                    return 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int executeUpdate(String namespace, String sqlId) {
        return executeUpdate(namespace, sqlId, null);
    }

    public Object execute(String namespace, String sqlId, Map<String, Object> data) {
        try {
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                if (!sqlMap.get(namespace).containsKey(sqlId)) {
                    throw new RuntimeException("sqlId not found : " + sqlId);
                } else {
                    ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                    if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                        throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                    }
                    ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                    // TODO: Replace DynamicSqlEngine.parse()
                    // SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), data);
                    // return SqlApiEngine.execute(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object execute(String namespace, String sqlId) {
        return execute(namespace, sqlId, null);
    }

    // TODO: Implement transaction support with RuoYi's data source
    /*
    public List<Object> executeWithinTransaction(List<SqlWithParam> sqlList) {
        DruidPooledConnection connection = null;
        try {
            String namespace = sqlList.get(0).getNamespace();
            connection = getConnection(namespace, sqlList.get(0).getSqlId());
            connection.setAutoCommit(false);
            if (!sqlMap.containsKey(namespace)) {
                throw new RuntimeException("namespace not found : " + namespace);
            } else {
                List<Object> result = new ArrayList<>();
                for (int i = 0; i < sqlList.size(); i++) {
                    String sqlId = sqlList.get(i).getSqlId();
                    if (!sqlMap.get(namespace).containsKey(sqlId)) {
                        throw new RuntimeException("sqlId not found : " + sqlId);
                    } else {
                        ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                        SqlMeta sqlMeta = dynamicSqlEngine.parse(sql.getText(), sqlList.get(i).getParameters());
                        Object object = JdbcUtil.execute(connection, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                        result.add(object);
                    }
                }
                connection.commit();
                return result;
            }
        } catch (Exception e) {
            try { if (connection != null) connection.rollback(); } catch (SQLException ex) { throw new RuntimeException(ex); }
            throw new RuntimeException(e.getMessage());
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException e) { throw new RuntimeException(e); }
        }
    }
    */

    private DruidPooledConnection getConnection(String namespace, String sqlId) throws SQLException {
        if (!sqlMap.containsKey(namespace)) {
            throw new RuntimeException("namespace not found : " + sqlId);
        } else {
            if (!sqlMap.get(namespace).containsKey(sqlId)) {
                throw new RuntimeException("sqlId not found : " + sqlId);
            } else {
                ApiSql sql = this.sqlMap.get(namespace).get(sqlId);
                if (!dataSourceMap.containsKey(sql.getDatasourceId())) {
                    throw new RuntimeException("datasource not found : " + sql.getDatasourceId());
                }
                ApiDataSource dataSource = dataSourceMap.get(sql.getDatasourceId());
                DruidPooledConnection connection = PoolManager.getPooledConnection(dataSource);
                return connection;
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------------

    public static List<JSONObject> executeQuery(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return JdbcUtil.executeQuery(connection,sql,jdbcParamValues);
    }

    public static JSONObject executeQueryById(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return JdbcUtil.executeQueryById(connection,sql,jdbcParamValues);
    }
    public static Long executeQueryInt(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        Object obj = executeQueryOneColumn(datasource, sql, jdbcParamValues);
        if(obj instanceof Long){
            return (Long) obj;
        }else{
            return Long.valueOf(String.valueOf(obj));
        }
    }

    public static String executeQueryString(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        Object obj = executeQueryOneColumn(datasource, sql, jdbcParamValues);
        return (String) obj;
    }

    public static Object executeQueryOneColumn(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return JdbcUtil.executeQueryOneColumn(connection,sql,jdbcParamValues);
    }

    public static int executeUpdate(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return JdbcUtil.executeUpdate(connection,sql,jdbcParamValues);
    }

    public static Object execute(ApiDataSource datasource, String sql, List<Object> jdbcParamValues) {
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return JdbcUtil.execute(connection,sql,jdbcParamValues);
    }


}