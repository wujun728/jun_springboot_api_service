package com.zapi.dataset.support.service;



import com.zapi.dataset.config.DataSourceConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDatabaseExportService implements DatabaseExportService {

    @Override
    public String exportScript(DataSourceConfig config, boolean exportData, List<String> selectedTables) throws SQLException {
        StringBuilder script = new StringBuilder();

        try (Connection conn = getConnection(config)) {
            // 获取表结构
            script.append(generateTableDDL(conn, config.getSchema(), selectedTables));

            if (exportData) {
                // 获取表数据
                script.append(generateTableData(conn, config.getSchema(), selectedTables));
            }
        }

        return script.toString();
    }

    protected Connection getConnection(DataSourceConfig config) throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    protected abstract String generateTableDDL(Connection conn, String schema, List<String> selectedTables) throws SQLException;

    protected abstract String generateTableData(Connection conn, String schema, List<String> selectedTables) throws SQLException;

    protected String getTableListCondition(List<String> selectedTables) {
        if (selectedTables == null || selectedTables.isEmpty()) {
            return "";
        }
        return String.join(",", selectedTables.stream().map(t -> "'" + t + "'").collect(Collectors.toList()));
    }
}