package com.gitee.freakchicken.util;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.entity.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {

    private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public static List<JSONObject> executeQuery(DataSource datasource, String sql, List<Object> jdbcParamValues) {
        logger.debug(sql);
        DruidPooledConnection connection = null;
        try {

            connection = PoolManager.getPooledConnection(datasource);
            connection.setAutoCommit(true);
            PreparedStatement statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }

            ResultSet rs = statement.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List list = new ArrayList();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.stream().forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        jo.put(t, value);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
                list.add(jo);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static int executeUpdate(DataSource datasource, String sql, List<Object> jdbcParamValues) {
        logger.debug(sql);
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
            connection.setAutoCommit(true);
            PreparedStatement statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }
            int i = statement.executeUpdate();
            return i;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    public static Object execute(DataSource datasource, String sql, List<Object> jdbcParamValues) {
        logger.debug(sql);
        DruidPooledConnection connection = null;
        try {
            connection = PoolManager.getPooledConnection(datasource);
            connection.setAutoCommit(true);
            PreparedStatement statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }
            boolean hasResultSet = statement.execute();
            if (hasResultSet) {
                ResultSet rs = statement.getResultSet();
                int columnCount = rs.getMetaData().getColumnCount();

                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnLabel(i);
                    columns.add(columnName);
                }
                List<JSONObject> list = new ArrayList<>();
                while (rs.next()) {
                    JSONObject jo = new JSONObject();
                    columns.stream().forEach(t -> {
                        try {
                            Object value = rs.getObject(t);
                            jo.put(t, value);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                    list.add(jo);
                }
                return list;
            } else {
                int updateCount = statement.getUpdateCount();
                return updateCount;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }


    public static Object execute(Connection connection, String sql, List<Object> jdbcParamValues) throws SQLException {
        logger.debug(sql);

        PreparedStatement statement = connection.prepareStatement(sql);
        //参数注入
        for (int i = 1; i <= jdbcParamValues.size(); i++) {
            statement.setObject(i, jdbcParamValues.get(i - 1));
        }
        boolean hasResultSet = statement.execute();
        if (hasResultSet) {
            ResultSet rs = statement.getResultSet();
            int columnCount = rs.getMetaData().getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.stream().forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        jo.put(t, value);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
                list.add(jo);
            }
            return list;
        } else {
            int updateCount = statement.getUpdateCount();
            return updateCount;
        }


    }
}