package com.zapi.dataset.support.service;


import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.dataTypeEnum.DmTypeEnum;
import com.zapi.dataset.model.vo.FieldInfoVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DmMetaService implements DatabaseMetaService {

    @Override
    public List<String> getTableList(DataSourceConfig config) throws SQLException {
        List<String> tables = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getTables(
                    null,
                    config.getSchema(),
                    null,
                    new String[]{"TABLE"})) {

                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
        }

        return tables;
    }

    @Override
    public List<HashMap<String, Object>> getTableSAndComment(DataSourceConfig config) {
        return null;
    }

    @Override
    public List<FieldInfoVO> getTableColumnList(DataSourceConfig config) throws SQLException {
        return null;
    }

    @Override
    public List<String> getColumnType() {
        List<String> collect = Arrays.stream(DmTypeEnum.values()).map(DmTypeEnum::getTypeName).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean updateColumnType(DataSourceConfig config) {
        return false;
    }

    @Override
    public boolean addTable(DataSourceConfig config) {
        return false;
    }

    @Override
    public boolean dropTable(DataSourceConfig config) {
        return false;
    }

    @Override
    public boolean insertBusinessData(DataSourceConfig config, String json) throws SQLException {
        return false;
    }
}
