package com.zapi.dataset.support.service;

import java.sql.*;
import java.util.List;

public class MySQLExportService extends AbstractDatabaseExportService {
    @Override
    protected String generateTableDDL(Connection conn, String schema, List<String> selectedTables) throws SQLException {
        StringBuilder ddl = new StringBuilder();
        DatabaseMetaData metaData = conn.getMetaData();

        try (ResultSet tables = metaData.getTables(schema, null, null, new String[]{"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME").toLowerCase();

                if (!selectedTables.isEmpty() && !selectedTables.contains(tableName)) {
                    continue;
                }

                ddl.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n");

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE `" + tableName + "`")) {
                    if (rs.next()) {
                        String createTable = rs.getString("Create Table");
                        // 将表名和字段名转换为小写
                        createTable = convertNamesToLowerCase(createTable);
                        ddl.append(createTable).append(";\n\n");
                    }
                }
            }
        }

        return ddl.toString();
    }

    @Override
    protected String generateTableData(Connection conn, String schema, List<String> selectedTables) throws SQLException {
        StringBuilder data = new StringBuilder();
        DatabaseMetaData metaData = conn.getMetaData();

        try (ResultSet tables = metaData.getTables(schema, null, null, new String[]{"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME").toLowerCase();

                if (!selectedTables.isEmpty() && !selectedTables.contains(tableName)) {
                    continue;
                }

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM `" + tableName + "`")) {

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // 构建插入语句的字段名部分，转换为小写
                    StringBuilder columns = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) columns.append(", ");
                        columns.append("`").append(rsmd.getColumnName(i).toLowerCase()).append("`");
                    }

                    while (rs.next()) {
                        data.append("INSERT INTO `").append(tableName).append("` (").append(columns).append(") VALUES (");
                        for (int i = 1; i <= columnCount; i++) {
                            if (i > 1) data.append(", ");
                            Object value = rs.getObject(i);
                            if (value == null) {
                                data.append("NULL");
                            } else {
                                String strValue = value.toString();
                                // 处理特殊字符
                                strValue = strValue.replace("'", "''");
                                data.append("'").append(strValue).append("'");
                            }
                        }
                        data.append(");\n");
                    }
                    data.append("\n");
                }
            }
        }

        return data.toString();
    }

    // 将建表语句中的表名和字段名转换为小写
    private String convertNamesToLowerCase(String createTable) {
        StringBuilder result = new StringBuilder();
        boolean inBacktick = false;
        int startIndex = 0;

        for (int i = 0; i < createTable.length(); i++) {
            char c = createTable.charAt(i);
            if (c == '`') {
                if (inBacktick) {
                    // 结束反引号，将其中的名称转换为小写
                    String name = createTable.substring(startIndex, i);
                    result.append("`").append(name.toLowerCase()).append("`");
                    inBacktick = false;
                } else {
                    // 开始反引号
                    startIndex = i + 1;
                    inBacktick = true;
                }
            } else if (!inBacktick) {
                result.append(c);
            }
        }

        return result.toString();
    }
}