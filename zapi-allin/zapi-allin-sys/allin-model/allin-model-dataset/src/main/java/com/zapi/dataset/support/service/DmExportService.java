package com.zapi.dataset.support.service;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.List;

public class DmExportService extends AbstractDatabaseExportService {

    @Override
    protected String generateTableDDL(Connection conn, String schema, List<String> selectedTables) throws SQLException {
        StringBuilder ddl = new StringBuilder();
        DatabaseMetaData metaData = conn.getMetaData();

        String tableCondition = getTableListCondition(selectedTables);

        // 获取表结构
        try (ResultSet tables = metaData.getTables(null, schema, null, new String[]{"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                if (!selectedTables.isEmpty() && !selectedTables.contains(tableName)) {
                    continue;
                }

               // ddl.append("-- Table structure for table \"").append(tableName).append("\"\n");
                ddl.append("DROP TABLE IF EXISTS \"").append(tableName).append("\";\n");

                // 获取列信息
                ddl.append("CREATE TABLE \"").append(tableName).append("\" (\n");

                try (ResultSet columns = metaData.getColumns(null, schema, tableName, null)) {
                    boolean first = true;
                    while (columns.next()) {
                        if (!first) {
                            ddl.append(",\n");
                        }
                        first = false;

                        String columnName = columns.getString("COLUMN_NAME");
                        String typeName = columns.getString("TYPE_NAME");
                        int columnSize = columns.getInt("COLUMN_SIZE");
                        int nullable = columns.getInt("NULLABLE");
                        String remarks = columns.getString("REMARKS");

                        ddl.append("  \"").append(columnName).append("\" ").append(typeName);
                        if (typeName.equals("VARCHAR") || typeName.equals("CHAR")) {
                            ddl.append("(").append(columnSize).append(")");
                        }

                        if (nullable == DatabaseMetaData.columnNoNulls) {
                            ddl.append(" NOT NULL");
                        }

                        if (remarks != null && !remarks.isEmpty()) {
                            ddl.append(" COMMENT '").append(remarks).append("'");
                        }
                    }
                }

                // 获取主键信息
                try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, schema, tableName)) {
                    StringBuilder pkColumns = new StringBuilder();
                    while (primaryKeys.next()) {
                        if (pkColumns.length() > 0) {
                            pkColumns.append(", ");
                        }
                        pkColumns.append("\"").append(primaryKeys.getString("COLUMN_NAME")).append("\"");
                    }
                    if (pkColumns.length() > 0) {
                        ddl.append(",\n  PRIMARY KEY (").append(pkColumns).append(")");
                    }
                }

                ddl.append("\n);\n\n");
            }
        }

        return ddl.toString();
    }

    @Override
    protected String generateTableData(Connection conn, String schema, List<String> selectedTables) throws SQLException {
        StringBuilder data = new StringBuilder();
        DatabaseMetaData metaData = conn.getMetaData();

        try (ResultSet tables = metaData.getTables(null, schema, null, new String[]{"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                if (!selectedTables.isEmpty() && !selectedTables.contains(tableName)) {
                    continue;
                }

               // data.append("-- Data for table \"").append(tableName).append("\"\n");

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM \"" + tableName + "\"")) {

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        data.append("INSERT INTO \"").append(tableName).append("\" VALUES (");
                        for (int i = 1; i <= columnCount; i++) {
                            if (i > 1) data.append(", ");
                            Object value = rs.getObject(i);
                            if (value == null) {
                                data.append("NULL");
                            } else {
                                int columnType = rsmd.getColumnType(i); // 获取列的数据类型
                                switch (columnType) {
                                    case Types.BLOB:
                                        Blob blob = rs.getBlob(i);
                                        try {
                                            byte[] bytes = blob.getBytes(1, (int) blob.length());
                                            // 使用 Hex 编码或其他方式编码二进制数据
                                            data.append("X'");
                                            data.append(bytesToHex(bytes)); // 假设有一个将字节数组转换为十六进制字符串的方法
                                            data.append("'");
                                        } catch (SQLException e) {
                                            data.append("'无法读取 BLOB 数据'");
                                        }
                                        break;
                                    case Types.CLOB:
                                        Clob clob = rs.getClob(i);
                                        StringBuilder clobData = new StringBuilder();
                                        Reader reader = clob.getCharacterStream();
                                        char[] buffer = new char[1024];
                                        int length;
                                        try {
                                            while ((length = reader.read(buffer)) != -1) {
                                                clobData.append(buffer, 0, length);
                                            }
                                            // 移除所有空白字符（包括空格、换行符、制表符等）
                                            String strValue = clobData.toString().replaceAll("\\s+", " ");
                                            strValue = strValue.replace("'", "''");
                                            data.append("'").append(strValue).append("'");
                                        } catch (IOException e) {
                                            data.append("'无法读取 CLOB 数据'");
                                        }
                                        break;
                                    default:
                                        String strValue = value.toString().replace("'", "''");
                                        data.append("'").append(strValue).append("'");
                                        break;

                                }
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

    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}