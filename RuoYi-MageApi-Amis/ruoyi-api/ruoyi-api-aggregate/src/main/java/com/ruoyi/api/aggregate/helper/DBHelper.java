package com.ruoyi.api.aggregate.helper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {

    private final DataSource dataSource;

    public DBHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> executeSql(String sql) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setMaxRows(2000);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int cols = metaData.getColumnCount();
                List<String> colNames = new ArrayList<>();
                for (int i = 0; i < cols; i++) {
                    colNames.add(metaData.getColumnLabel(i + 1));
                }
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 0; i < cols; i++) {
                        row.put(colNames.get(i), rs.getObject(colNames.get(i)));
                    }
                    resultList.add(row);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("SQL execution failed: " + e.getMessage(), e);
        }
        return resultList;
    }
}