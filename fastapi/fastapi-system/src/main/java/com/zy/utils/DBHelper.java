package com.zy.utils;

import com.zy.base.domain.DbConfig;
import com.zy.iservice.domain.IntfaceReverseHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {

    public Object executeSql(DbConfig dbConfig, String sql, IntfaceReverseHistory intfaceReverseHistory){
        //sql调用耗时开始时间
        long reverseStartTime = System.currentTimeMillis();

        Connection conn = null;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();

        try {
            Class.forName(dbConfig.getConnectDriver());

            conn = DriverManager.getConnection(dbConfig.getConnectUrl(), dbConfig.getUserName(), dbConfig.getPassword());

            preparedStatement = conn.prepareStatement(sql);
            //限制数据最大查询条数,防止oom,目前设置为2000条
            preparedStatement.setMaxRows(2000);

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();

            List<String> colNameList=new ArrayList<String>();
            int cols_len = metaData.getColumnCount(); // 获取总的列数
            for (int i = 0; i < cols_len; i++) {
                //String col_name = metaData.getColumnName(i + 1); // 获取第 i列的字段名称
                //String col_Tpye = metaData.getColumnTypeName(i+1);//类型
                colNameList.add(metaData.getColumnLabel(i+1));
            }

            while (resultSet.next()) {
                Map<String, Object> map=new HashMap<String, Object>();
                for(int i=0;i<cols_len;i++){
                    String key=colNameList.get(i);
                    Object value=resultSet.getObject(colNameList.get(i));
                    map.put(key, value);
                }
                resultList.add(map);
            }

            //设置接口调用状态为成功
            intfaceReverseHistory.setReverseStatus(1L);

            //sql调用耗时结束时间
            long reverseEndTime = System.currentTimeMillis();

            intfaceReverseHistory.setReverseTime(reverseEndTime - reverseStartTime);

        } catch (Exception e) {

            //设置接口调用状态为失败
            intfaceReverseHistory.setReverseStatus(2L);
            //设置接口调用耗时为-1
            intfaceReverseHistory.setReverseTime(-1L);
            e.printStackTrace();
        }finally {
            try {
                if(null!=resultSet){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(null!=preparedStatement){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(null!=conn){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

}
