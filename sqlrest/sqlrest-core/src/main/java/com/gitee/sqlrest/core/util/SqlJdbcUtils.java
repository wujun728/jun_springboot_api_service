package com.gitee.sqlrest.core.util;

import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.template.SqlMeta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SqlJdbcUtils {

  public static boolean isQuerySQL(String sql) {
    String upperSql = sql.toUpperCase().trim();
    return upperSql.startsWith("SELECT") || upperSql.startsWith("WITH");
  }

  public static Function<String, String> getConverter(NamingStrategyEnum strategy) {
    return (null == strategy) ? Function.identity() : strategy.getFunction();
  }

  public static Object execute(ProductTypeEnum productType, Connection connection, SqlMeta sqlMeta,
      NamingStrategyEnum strategy, int page, int size)
      throws SQLException {
    List<Object> paramValues = sqlMeta.getParameter();
    boolean isQuerySql = isQuerySQL(sqlMeta.getSql());
    String sql = isQuerySql ? productType.getPageSql(sqlMeta.getSql()) : sqlMeta.getSql();
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setQueryTimeout(300);
    statement.setFetchSize(isMySqlConnection(connection) ? Integer.MIN_VALUE : 100);
    if (isQuerySql) {
      if (page <= 0) {
        page = 1;
      }
      if (size <= 0) {
        size = 10;
      }
      paramValues.add(size);
      paramValues.add((page - 1) * size);
    }
    for (int i = 1; i <= paramValues.size(); i++) {
      statement.setObject(i, paramValues.get(i - 1));
    }

    log.info("ExecuteSQL:{}\n{}", sql, paramValues);
    Function<String, String> converter = getConverter(strategy);
    if (statement.execute()) {
      try (ResultSet rs = statement.getResultSet()) {
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          String columnName = rs.getMetaData().getColumnLabel(i);
          columns.add(columnName);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
          Map<String, Object> row = new LinkedHashMap<>();
          for (String column : columns) {
            try {
              row.put(column, rs.getObject(column));
            } catch (SQLException se) {
              log.warn("Failed to call jdbc ResultSet::getObject(): {}", se.getMessage(), se);
              row.put(column, null);
            }
          }
          list.add(ConvertUtils.to(row, converter));
        }
        return list;
      }
    } else {
      int updateCount = statement.getUpdateCount();
      return "(" + updateCount + ") rows affected";
    }
  }

  private boolean isMySqlConnection(Connection connection) {
    try {
      String productName = connection.getMetaData().getDatabaseProductName();
      return productName.contains("MySQL") || productName.contains("MariaDB");
    } catch (Exception e) {
      return false;
    }
  }

}
