package com.zapi.dataset.support.service;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.dataTypeEnum.MySqlTypeEnum;
import com.zapi.dataset.model.vo.FieldInfoVO;
import cn.hutool.core.util.ObjectUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySQLMetaService implements DatabaseMetaService {

    @Override
    public List<String> getTableList(DataSourceConfig config) throws SQLException {
        List<String> tables = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            // 从URL中提取数据库名(如果schema为空)
            String dbName = config.getSchema();
            if (dbName == null || dbName.isEmpty()) {
                dbName = extractDatabaseNameFromUrl(config.getUrl());
            }

            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getTables(
                    dbName,  // 使用提取的数据库名
                    null,
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
    public List<HashMap<String, Object>> getTableSAndComment(DataSourceConfig config) throws SQLException {
        List<HashMap<String,Object>> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            // 从URL中提取数据库名(如果schema为空)
            String dbName = config.getSchema();
            if (dbName == null || dbName.isEmpty()) {
                dbName = extractDatabaseNameFromUrl(config.getUrl());
            }
            /**
             * SELECT
             * 	TABLE_NAME AS 表名,
             * 	TABLE_COMMENT AS 表注释
             * FROM
             * 	INFORMATION_SCHEMA.TABLES
             * WHERE
             * 	TABLE_SCHEMA = 'zapi_data_source'
             */
            String sql = "select TABLE_NAME , TABLE_COMMENT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = " + "'"  + dbName + "'";
            ResultSet resultSet = conn.prepareStatement(sql).executeQuery();

            while (resultSet.next()) {
                HashMap<String,Object> map = new HashMap<>();
                String tableName = resultSet.getString("TABLE_NAME");
                String tableComment = resultSet.getString("TABLE_COMMENT");
                map.put("tableName", tableName);
                map.put("tableComment", tableComment);
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List<FieldInfoVO> getTableColumnList(DataSourceConfig config) throws SQLException {
        List<FieldInfoVO> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            // 从URL中提取数据库名(如果schema为空)
            String dbName = config.getSchema();
            if (dbName == null || dbName.isEmpty()) {
                dbName = extractDatabaseNameFromUrl(config.getUrl());
            }
            /**
             * SELECT
             * 	COLUMN_NAME,
             * 	COLUMN_TYPE,
             * 	COLUMN_COMMENT
             * FROM
             * 	INFORMATION_SCHEMA.COLUMNS
             * WHERE
             * 	TABLE_SCHEMA = 'zapi_data_source'
             * 	AND TABLE_NAME = 't_data_source';
             */
            String sql = "select COLUMN_NAME , COLUMN_TYPE , COLUMN_COMMENT from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA = "
                    + "'"  + dbName + "'"
                    + " and TABLE_NAME = '" + config.getTableName() + "'";
            ResultSet resultSet = conn.prepareStatement(sql).executeQuery();

            while (resultSet.next()) {
                FieldInfoVO infoVO = new FieldInfoVO();
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnType = resultSet.getString("COLUMN_TYPE");
                String columnComment = resultSet.getString("COLUMN_COMMENT");
                infoVO.setColumnName(columnName);
                infoVO.setColumnType(columnType);
                infoVO.setColumnComment(columnComment);
                list.add(infoVO);
            }

            for (int i = 0; i < list.size(); i++) {
                // 设置序号
                list.get(i).setNum(i);
            }

        }
        return list;
    }

    @Override
    public List<String> getColumnType() {
        List<String> collect = Arrays.stream(MySqlTypeEnum.values()).map(MySqlTypeEnum::getTypeName).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean updateColumnType(DataSourceConfig config) throws SQLException {

        // 字段信息 , 新增、删除、修改
        ArrayList<FieldInfoVO> addList = new ArrayList<>(); // 新增字段
        ArrayList<FieldInfoVO> delList = new ArrayList<>(); // 删除字段
        ArrayList<FieldInfoVO> updateModifyList = new ArrayList<>(); // 修改字段类型
        ArrayList<FieldInfoVO> updateRenameList = new ArrayList<>(); // 修改字段名称
        ArrayList<FieldInfoVO> updateCommentList = new ArrayList<>(); // 修改字段注释

        // 现在数据库存在的数据
        List<FieldInfoVO> fieldList = this.getTableColumnList(config);
        // 前端更新后返回的数据
        List<FieldInfoVO> fields = config.getFields();

        for (FieldInfoVO pModelColumn : fieldList) {
            // 判断是否有删除数据
            Boolean deleteFlag = false;
            for (FieldInfoVO column : fields) {
                if (ObjectUtil.isEmpty(column.getNum())){
                    // 新增字段，并去重
                    boolean exists  = addList.stream().anyMatch(element -> element.equals(column));
                    if (!exists){
                        addList.add(column);
                    }
                    // 注释
                    boolean commentExists = updateCommentList.stream().anyMatch(element -> element.equals(column));
                    if (!commentExists){
                        updateCommentList.add(column);
                    }
                }else {
                    // 修改字段，并去重
                    if ((pModelColumn.getNum().equals(column.getNum()) && !pModelColumn.getColumnType().equals(column.getColumnType()))
                            || (pModelColumn.getNum().equals(column.getNum()) && (!(StringUtils.isNotEmpty(pModelColumn.getColumnComment())?pModelColumn.getColumnComment():"").equals(column.getColumnComment())))
                    ){
                        boolean exists  = updateModifyList.stream().anyMatch(element -> element.equals(column));
                        if (!exists){

                            updateModifyList.add(column);
                        }
                        updateModifyList.add(column);

                    }
                    // 修改名称
                    if (pModelColumn.getNum().equals(column.getNum()) && !pModelColumn.getColumnName().equals(column.getColumnName())){
                        boolean exists  = updateRenameList.stream().anyMatch(element -> element.equals(column));
                        if (!exists){
                            column.setOldName(pModelColumn.getColumnName());
                            updateRenameList.add(column);
                        }
                    }

                }
                if (pModelColumn.getNum().equals(column.getNum())){
                    deleteFlag = true;
                }
            }
            if (!deleteFlag){
                // 删除字段
                delList.add(pModelColumn);
            }
        }

        try {
            this.alterColumnName(config,updateRenameList);
            this.alterColumnType(config,updateModifyList);
            this.dropColumn(config,delList);
            this.addColumn(config,addList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean addTable(DataSourceConfig config) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE " + config.getTableName() + " (ID varchar(32) NOT NULL COMMENT '主键id', PRIMARY KEY (ID)) ";
            if (StringUtils.isNotEmpty(config.getTableComment())){
                sql += " COMMENT '" + config.getTableComment() + "' ";
            }
            stmt.execute(sql);
            return true;
        }catch (Exception e){
            throw new RuntimeException("新增表失败");
        }
    }

    @Override
    public boolean dropTable(DataSourceConfig config) {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE " + config.getTableName() );
            return true;
        }catch (Exception e){
            throw new RuntimeException("删除表失败");
        }
    }

    @Override
    public boolean insertBusinessData(DataSourceConfig config, String json) throws SQLException {
        // 1. 解析json为Map
        Map<String, Object> map = com.alibaba.fastjson2.JSONObject.parseObject(json, Map.class);
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("json数据不能为空");
        }
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            key.append(entry.getKey()).append(",");
            value.append("'").append(entry.getValue() == null ? "" : entry.getValue().toString()).append("',");
        }
        key.delete(key.length() - 1, key.length());
        value.delete(value.length() - 1, value.length());
        String sql = "insert into " + config.getTableName() + "(" + key.toString() + ") VALUES (" + value.toString() + ")";
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(sql);
            return result > 0;
        }
    }

    /**
     * 新增字段
     * @param config
     * @param addList
     * @throws SQLException
     */
    private void addColumn(DataSourceConfig config, ArrayList<FieldInfoVO> addList) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            Statement stmt = conn.createStatement();

            /**
             * // ALTER TABLE ${param.tableName} ADD "${param.colmunName}" ${param.colmunType} DEFAULT NULL
             */
            for (FieldInfoVO fieldInfoVO : addList) {
                // 字符类型处理
                if (fieldInfoVO.getColumnType().equals(MySqlTypeEnum.VARCHAR.getTypeName())){
                    fieldInfoVO.setColumnType(fieldInfoVO.getColumnType()+"(255)");
                }
                String sql = "ALTER TABLE " + config.getTableName() + " ADD COLUMN " + fieldInfoVO.getColumnName() + " " + fieldInfoVO.getColumnType() + " DEFAULT NULL";
                if (StringUtils.isNotEmpty(fieldInfoVO.getColumnComment())){
                    sql += " COMMENT '" + fieldInfoVO.getColumnComment() + "' ";
                }
                boolean execute = stmt.execute(sql);
            }
        }
    }

    private void dropColumn(DataSourceConfig config, ArrayList<FieldInfoVO> delList) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            Statement stmt = conn.createStatement();

            /**
             * // ALTER TABLE ${param.tableName} DROP COLUMN "${param.colmunName}";
             */
            for (FieldInfoVO fieldInfoVO : delList) {
                boolean execute = stmt.execute("ALTER TABLE " + config.getTableName() + " DROP COLUMN " + fieldInfoVO.getColumnName());
            }
        }
    }

    private void alterColumnType(DataSourceConfig config, ArrayList<FieldInfoVO> updateModifyList ) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            Statement stmt = conn.createStatement();

            /**
             * ALTER TABLE ${param.tableName} MODIFY "${param.colmunName}" ${param.colmunType};
             */
            for (FieldInfoVO fieldInfoVO : updateModifyList) {
                if (fieldInfoVO.getColumnType().equals(MySqlTypeEnum.VARCHAR.getTypeName())){
                    fieldInfoVO.setColumnType(fieldInfoVO.getColumnType()+"(255)");
                }
                String sql = "ALTER TABLE " + config.getTableName() + " MODIFY " + fieldInfoVO.getColumnName()+ " " + fieldInfoVO.getColumnType();
                if (StringUtils.isNotEmpty(fieldInfoVO.getColumnComment())){
                    sql += " COMMENT '" + fieldInfoVO.getColumnComment() + "' ";
                }
                boolean execute = stmt.execute(sql);
            }
        }
    }

    private void alterColumnName(DataSourceConfig config, ArrayList<FieldInfoVO> updateRenameList) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            Statement stmt = conn.createStatement();

            /**
             *  ALTER TABLE ${param.tableName} RENAME COLUMN "${param.oldName}" TO "${param.newName}";
             */
            for (FieldInfoVO fieldInfoVO : updateRenameList) {
                String sql = "ALTER TABLE " + config.getTableName() + "  RENAME COLUMN " + fieldInfoVO.getOldName() + " TO " + fieldInfoVO.getColumnName();
                boolean execute = stmt.execute(sql);
            }
        }
    }

    // 从JDBC URL中提取数据库名
    private String extractDatabaseNameFromUrl(String url) {
        // jdbc:mysql://localhost:3306/databaseName?param=value
        String[] parts = url.split("\\?")[0].split("/");
        return parts[parts.length - 1];
    }
}
