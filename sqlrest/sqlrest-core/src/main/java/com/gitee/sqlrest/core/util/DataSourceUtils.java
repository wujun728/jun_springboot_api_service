package com.gitee.sqlrest.core.util;

import cn.hutool.core.util.ClassLoaderUtil;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.common.model.JarFileClassLoader;
import com.gitee.sqlrest.common.model.SimpleDataSource;
import com.gitee.sqlrest.persistence.entity.DataSourceEntity;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@UtilityClass
public final class DataSourceUtils {

  public static final int MAX_THREAD_COUNT = 10;
  public static final int MAX_TIMEOUT_MS = 60000;

  private static final Map<String, URLClassLoader> classLoaderMap = new ConcurrentHashMap<>();
  private static final Map<Long, Pair<DataSourceEntity, HikariDataSource>> datasourceMap = new ConcurrentHashMap<>();

  public static HikariDataSource getHikariDataSource(DataSourceEntity entity, String driverPath) {
    if (!datasourceMap.containsKey(entity.getId())) {
      HikariDataSource ds = createDataSource(entity, driverPath);
      try (Connection connection = ds.getConnection()) {
        connection.isValid(2);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      datasourceMap.put(entity.getId(), Pair.of(entity, ds));
    } else {
      if (!DataSourceEntity.isSame(entity, datasourceMap.get(entity.getId()).getKey())) {
        dropHikariDataSource(entity.getId());
        return getHikariDataSource(entity, driverPath);
      }
    }
    return datasourceMap.get(entity.getId()).getRight();
  }

  public static void dropHikariDataSource(Long dataSourceId) {
    Pair<DataSourceEntity, HikariDataSource> dsPair = datasourceMap.remove(dataSourceId);
    if (null != dsPair) {
      try {
        dsPair.getRight().close();
      } catch (Exception e) {
        log.warn("Error when close HikariDataSource:{}", e.getMessage());
      }
    }
  }

  private static HikariDataSource createDataSource(DataSourceEntity properties, String driverPath) {
    Properties parameters = new Properties();
    HikariDataSource ds = new HikariDataSource();
    ds.setPoolName("The_JDBC_Connection");
    ds.setJdbcUrl(properties.getUrl());
    if (ProductTypeEnum.ORACLE == properties.getType()) {
      ds.setConnectionTestQuery(properties.getType().getTestSql());
      // https://blog.csdn.net/qq_20960159/article/details/78593936
      System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
      // Oracle在通过jdbc连接的时候需要添加一个参数来设置是否获取注释
      parameters.put("remarksReporting", "true");
    } else if (StringUtils.isNotBlank(properties.getType().getTestSql())) {
      ds.setConnectionTestQuery(properties.getType().getTestSql());
    }
    ds.setMaximumPoolSize(MAX_THREAD_COUNT);
    ds.setMinimumIdle(MAX_THREAD_COUNT);
    ds.setMaxLifetime(TimeUnit.MINUTES.toMillis(60));
    ds.setConnectionTimeout(TimeUnit.SECONDS.toMillis(60));
    ds.setIdleTimeout(MAX_TIMEOUT_MS);

    SimpleDataSource dataSource = new SimpleDataSource(
        createURLClassLoader(driverPath, properties.getDriver()),
        properties.getUrl(),
        properties.getDriver(),
        properties.getUsername(),
        properties.getPassword(),
        parameters
    );
    ds.setDataSource(dataSource);

    return ds;
  }

  private static URLClassLoader createURLClassLoader(String driverPath, String driverClass) {
    if (StringUtils.isBlank(driverPath)) {
      throw new RuntimeException("Invalid driver path,can not be empty!");
    }
    if (StringUtils.isBlank(driverClass)) {
      throw new RuntimeException("Invalid driver class,can not be empty!");
    }
    ClassLoader parent = ClassLoaderUtil.getSystemClassLoader().getParent();
    URLClassLoader loader = getOrCreateClassLoader(driverPath, parent);
    try {
      Class<?> clazz = loader.loadClass(driverClass);
      clazz.getConstructor().newInstance();
      return loader;
    } catch (Exception e) {
      log.error("Could not load class : {} from driver path: {}", driverClass, driverPath, e);
      throw new RuntimeException(e);
    }
  }

  private static URLClassLoader getOrCreateClassLoader(String path, ClassLoader parent) {
    URLClassLoader urlClassLoader = classLoaderMap.get(path);
    if (null == urlClassLoader) {
      synchronized (DataSourceUtils.class) {
        urlClassLoader = classLoaderMap.get(path);
        if (null == urlClassLoader) {
          log.info("Create jar classLoader from path: {}", path);
          urlClassLoader = new JarFileClassLoader(path, parent);
          classLoaderMap.put(path, urlClassLoader);
        }
      }
    }
    return urlClassLoader;
  }

}
