package com.itboy.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.itboy.model.DbSourceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DataSourceFactory
 * @Description 数据源管理
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 16:29
 **/
@Slf4j
public class DataSourceFactory {

    private static ConcurrentHashMap<String, DruidDataSource> map = new ConcurrentHashMap<String, DruidDataSource>();
    private static ConcurrentHashMap<String, String> dbType = new ConcurrentHashMap<String, String>();


    /**
     * 根据jdbc参数获得数据源连接池dbcp，并放入ConcurrentHashMap
     */
    @Primary
    public static void initDataSource(List<DbSourceModel> mapList) {
        DruidDataSource ds = null;
        int index = 0;
        for (DbSourceModel config : mapList) {
            ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbaccount().trim());
            ds.setPassword(config.getDbpass().trim());
            ds.setUrl(config.getDburl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery(config.getDbtestUrl());
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            ds.setNotFullTimeoutRetryCount(-1);
            try {
                ds.setFilters("stat,wall");
                ds.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
                ds.init();
                map.put(config.getDbname().trim(), ds);
                setDbType(config.getDriverClass().trim(), config.getDbname().trim());
                index++;
            } catch (SQLException e) {
                log.error(config.getDbname() + "数据源初始化失败：" + e.getMessage());
                DruidDataSourceStatManager.removeDataSource(ds);
            }
        }
        log.info("Successful initialization  " + index + " DbSources ");
    }


    /**
     * 插入数据源连接池到缓存
     */
    public static void saveDataSource(DbSourceModel config) throws SQLException {
        if (config != null) {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbaccount().trim());
            ds.setPassword(config.getDbpass().trim());
            ds.setUrl(config.getDburl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery("select 1");
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            ds.setFailFast(true);
            ds.init();
            map.put(config.getDbname().trim(), ds);
            setDbType(config.getDriverClass().trim(), config.getDbname().trim());
        }
    }


    /**
     * 从缓存里删除指定的数据源连接池
     */
    public static void removeDataSource(String sourceKey) {
        DruidDataSource ds = map.get(sourceKey.trim());
        if (ds == null) {
            return;
        }
        shutdownDataSource(ds);
        map.remove(sourceKey.trim());
        dbType.remove(sourceKey.trim());
        DruidDataSourceStatManager.removeDataSource(ds);

        log.info("Successful Delete  DbSources ");
    }


    /**
     * 修改数据源连接池到缓存,未实现修改方法
     */

    public static void updateDataSource(DbSourceModel config) throws Exception {
        if (config != null) {
            //先将原有数据源连接池从缓存中删除
            removeDataSource(config.getDbname());
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbaccount().trim());
            ds.setPassword(config.getDbpass().trim());
            ds.setUrl(config.getDburl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery("select 1");
            ds.setConnectionErrorRetryAttempts(1);
            ds.setNotFullTimeoutRetryCount(-1);
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            map.put(config.getDbname().trim(), ds);
        }
        log.info("Successful Update  DbSources ");
    }


    /**
     * 获得dbcp连接池
     */

    public static DruidDataSource getDataSource(String sourceKey) {
        DruidDataSource ds = map.get(sourceKey.trim());
        return ds;
    }

    public static Connection getConnection(DruidDataSource source) {
        Connection con = null;
        if (source != null) {
            try {
                con = source.getConnection();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return con;

        }
        return con;
    }

    protected static void shutdownDataSource(DruidDataSource source) {
        if (source != null) {
            source.close();
        }
    }

    private static void setDbType(String driverClassName, String sourceKey) {
        String dbName = "";
        if (driverClassName.indexOf("oracle") != -1) {
            dbName = "oracle";
        }
        if (driverClassName.indexOf("mysql") != -1) {
            dbName = "mysql";
        }
        if (driverClassName.indexOf("h2") != -1) {
            dbName = "h2";
        }
        dbType.put(sourceKey.trim(), dbName);
    }

    public static String getDbType(String sourceKey) {
        return dbType.get(sourceKey);
    }

}
