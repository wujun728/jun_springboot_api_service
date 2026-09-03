package com.gitee.sqlrest.common.model;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import lombok.Getter;

@Getter
public class SimpleDataSource implements DataSource {

  private final ClassLoader classLoader;
  private final String jdbcUrl;
  private final String driverClassName;
  private final Properties properties;

  public SimpleDataSource(ClassLoader cl, String jdbcUrl, String driverClassName, String username,
      String password, Properties properties) {
    this.classLoader = Objects.requireNonNull(cl, "parameter invalid for empty class loader");
    this.jdbcUrl = Objects.requireNonNull(jdbcUrl, "parameter invalid for empty jdbc url");
    this.driverClassName = Objects.requireNonNull(driverClassName, "parameter invalid for empty driver class name");
    this.properties = Objects.requireNonNull(properties, "parameter invalid for properties ");

    if (username != null) {
      this.properties.put("user", properties.getProperty("user", username));
    }
    if (password != null) {
      this.properties.put("password", properties.getProperty("password", password));
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return doGetConnection(properties);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    final Properties cloned = (Properties) properties.clone();
    if (username != null) {
      cloned.put("user", username);
      if (cloned.containsKey("username")) {
        cloned.put("username", username);
      }
    }
    if (password != null) {
      cloned.put("password", password);
    }

    return doGetConnection(cloned);
  }

  private Connection doGetConnection(Properties properties) throws SQLException {
    Driver driver = null;
    try {
      Class<?> driverType = Class.forName(driverClassName, true, classLoader);
      driver = (Driver) driverType.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new SQLException("Invalid driver class name [" + driverClassName + "]. Cause: " + e);
    }

    Connection connection = driver.connect(jdbcUrl, properties);
    if (null == connection) {
      throw new SQLException(
          "Maybe invalid driver class name [" + driverClassName + "] or url [" + jdbcUrl + "]");
    }

    return connection;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    DriverManager.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return DriverManager.getLoginTimeout();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

}
