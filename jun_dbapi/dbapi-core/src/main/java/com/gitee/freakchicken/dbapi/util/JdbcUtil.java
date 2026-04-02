package com.gitee.freakchicken.dbapi.util;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import lombok.extern.slf4j.Slf4j;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class JdbcUtil {

    private static final Pattern VALID_TABLE_NAME = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_.]*$");

    private static String validateTableName(String table) {
        if (table == null || table.trim().isEmpty()) {
            throw new IllegalArgumentException("表名不能为空");
        }
        String trimmed = table.trim();
        if (!VALID_TABLE_NAME.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("非法表名: " + trimmed);
        }
        if (trimmed.length() > 128) {
            throw new IllegalArgumentException("表名长度超过限制: " + trimmed);
        }
        return trimmed;
    }

    public static List<JSONObject> query(String sql, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnLabel(i));
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                for (String col : columns) {
                    jo.put(col, rs.getObject(col));
                }
                list.add(jo);
            }
            return list;
        }
    }

    public static Connection getConnection(DataSource ds) throws SQLException, ClassNotFoundException {
        String url = ds.getUrl();
        switch (ds.getType()) {
            case JdbcConstants.MYSQL:
                Class.forName(JdbcConstants.MYSQL_DRIVER);
                break;
            case JdbcConstants.POSTGRESQL:
                Class.forName(JdbcConstants.POSTGRESQL_DRIVER);
                break;
            case JdbcConstants.HIVE:
                Class.forName(JdbcConstants.HIVE_DRIVER);
                break;
            case JdbcConstants.SQL_SERVER:
                Class.forName(JdbcConstants.SQL_SERVER_DRIVER_SQLJDBC4);
                break;
            case JdbcConstants.CLICKHOUSE:
                Class.forName(JdbcConstants.CLICKHOUSE_DRIVER);
                break;
            case JdbcConstants.KYLIN:
                Class.forName(JdbcConstants.KYLIN_DRIVER);
                break;
            case JdbcConstants.ORACLE:
                Class.forName(JdbcConstants.ORACLE_DRIVER);
                break;
            default:
                break;
        }

        Connection connection = DriverManager.getConnection(url, ds.getUsername(), ds.getPassword());
        log.info("获取连接成功");
        return connection;
    }

    /**
     * 查询库中所有表
     * @param conn
     * @param type
     * @return
     */
    public static List<String> getAllTables(Connection conn, String type) {
        List<String> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            String sql;
            switch (type) {
                case "MYSQL":
                case "HIVE":
                    sql = "show tables";
                    break;
                case "POSTGRESQL":
                    sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name";
                    break;
                // TODO
                case "SQLSERVER":
                    sql = "select * from sys.tables";
                    break;
                case "ORACLE":
                    sql = "select table_name from user_tables where TABLESPACE_NAME is not null";
                    break;
                default:
                    sql = "show tables";

            }

            pst = conn.prepareStatement(sql);
            resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String s = resultSet.getString(1);
                list.add(s);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pst != null)
                    pst.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                log.error("关闭数据库资源失败", e);
            }
        }
    }

    /**
     * 查询表所有字段
     *
     * @param conn
     * @param type
     * @param table
     * @return
     */
    public static List<JSONObject> getRDBMSColumnProperties(Connection conn, String type, String table) {
        List<JSONObject> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String validTable = validateTableName(table);
            String sql;
            switch (type) {
                case "POSTGRESQL":
                    sql = "select * from \"" + validTable + "\" where 1=2";
                    break;
                default:
                    sql = "select * from `" + validTable + "` where 1=2";
            }
            log.info(sql);
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ResultSetMetaData rsd = rs.getMetaData();

            for (int i = 0; i < rsd.getColumnCount(); i++) {
                JSONObject jsonObject = new JSONObject();

                String columnTypeName = rsd.getColumnTypeName(i + 1);
                jsonObject.put("fieldTypeName", columnTypeName);//数据库字段类型名
                jsonObject.put("TypeName", columnTypeName);
                jsonObject.put("fieldJavaTypeName", rsd.getColumnClassName(i + 1));//映射到java的类型名
                String columnName = rsd.getColumnName(i + 1);
                if (columnName.contains("."))
                    columnName = columnName.split("\\.")[1];
                jsonObject.put("label", columnName);//表字段
                list.add(jsonObject);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pst != null)
                    pst.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                log.error("关闭数据库资源失败", e);
            }
        }
    }

    public static ResponseDto executeSql(DataSource datasource, String sql, List<Object> jdbcParamValues) {
        log.debug(sql);
        log.debug(JSON.toJSONString(jdbcParamValues));
        DruidPooledConnection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {

            connection = PoolManager.getPooledConnection(datasource);
            statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }

            boolean hasResultSet = statement.execute();
            if (hasResultSet) {
                rs = statement.getResultSet();
                final ResultSet resultSet = rs;
                int columnCount = resultSet.getMetaData().getColumnCount();

                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnLabel(i);
                    columns.add(columnName);
                }
                List<JSONObject> list = new ArrayList<>();
                while (resultSet.next()) {
                    JSONObject jo = new JSONObject();
                    columns.stream().forEach(t -> {
                        try {
                            Object value = resultSet.getObject(t);
                            jo.put(t, value);
                        } catch (SQLException throwables) {
                            log.error("读取结果集字段失败", throwables);
                        }
                    });
                    list.add(jo);
                }
                return ResponseDto.apiSuccess(list);
            } else {
                int updateCount = statement.getUpdateCount();
                return ResponseDto.apiSuccess("sql修改数据行数： " + updateCount);
            }
        } catch (Exception e) {
            log.error("执行SQL失败", e);
            return ResponseDto.fail(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException throwables) {
                log.error("关闭数据库资源失败", throwables);
            }
        }
    }
}