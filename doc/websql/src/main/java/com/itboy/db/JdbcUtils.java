package com.itboy.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JdbcUtils
 * @Description 数据库操作
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 16:31
 **/
@Slf4j
public class JdbcUtils {

    public static Map<String,Object> updateByPreparedStatement(String sourceKey, String sql, List<Object> params) {
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        map.put("msg","执行成功");
        Connection connection =  getConnections(sourceKey);
        int result = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            map.put("data",result);
        } catch (SQLException e) {
            log.error("执行异常,"+e.getMessage());
            map.put("code","2");
            map.put("msg",e.getMessage());
        } finally {
            releaseConn(null, pstmt, connection);
        }
        return map;

    }

    public static Map<String,Object> findMoreResult(String sourceKey, String sql, List<Object> params)  {
        Connection connection =  getConnections(sourceKey);
        Map<String,Object> map = new HashMap<>();
        JSONArray list = new JSONArray();
        map.put("code","1");
        map.put("msg","执行成功");
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try{
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            String cols_name;
            Object cols_value;
            while (resultSet.next()) {
                //update 2020.06.12 感谢Mr.Guo 提出顺序展示问题
                JSONObject json = new JSONObject(new LinkedHashMap());
                for (int i = 0; i < cols_len; i++) {
                    cols_name = metaData.getColumnLabel(i + 1);
                    cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    json.put(cols_name, cols_value);
                }
                list.add(json);
            }
            map.put("data",list);
        }catch (Exception e){
            log.error("执行异常,"+e.getMessage());
            map.put("code","2");
            map.put("msg",e.getMessage());
            map.put("data","执行失败,无返回结果.");
        }finally {
            releaseConn(resultSet, pstmt, connection);
        }
        return map;

    }

    /**
     * 执行计划
     * @param sourceKey 数据源
     * @param sql 执行sql
     * @param params 参数
     * @return
     */
    public static Map<String,Object> updateTimers(String sourceKey, String sql, List<Object> params,StringBuffer field)  {
        Connection connection =  getConnections(sourceKey);
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        map.put("msg","执行成功");
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement(sql);
            String[] items = field.toString().split(",");
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    int index = 1;
                    JSONObject item1 = (JSONObject) params.get(i);
                    for (int j = 0; j <items.length ; j++) {
                        Object itemObj = item1.get(items[j]);
                        if(itemObj==""){
                            itemObj=null;
                        }
                        pstmt.setObject(index++, itemObj);
                    }
                    pstmt.addBatch();//添加到批次
                }
                pstmt.executeBatch();
            }else{
                throw new NullPointerException("属性数据为空，无效插入.");
            }
        }catch (Exception e){
            log.error("执行异常,"+e.getMessage());
            map.put("code","2");
            map.put("msg",e.getMessage());
        }finally {
            releaseConn(null, pstmt, connection);
        }
        return map;

    }

    /**
     * 未使用。 通过count(1)获取记录总数
     */
    public static long getCount(String sourceKey, String sql, List<Object> params) {
        Connection connection =  getConnections(sourceKey);
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        long count = 0L;
        try {
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            releaseConn(resultSet, pstmt, connection);
        }
        return count;

    }

    public static void releaseConn(ResultSet resultSet, PreparedStatement pstmt, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    private static Connection getConnections(String sourceKey){
       DruidDataSource source = DataSourceFactory.getDataSource(sourceKey);
        if(source==null){
            throw new NullPointerException(sourceKey+"：未获取到有效数据源");
        }
        Connection connection = DataSourceFactory.getConnection(source);
        if(source==null){
            throw new NullPointerException(sourceKey+"：未获取到有效连接");
        }
        return connection;
    }

}

