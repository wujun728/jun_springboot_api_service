package com.chen.generator.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.chen.generator.model.ColumnEntity;
import com.chen.generator.model.DatasourceProperties;
import com.chen.generator.model.ExcelView;
import com.chen.generator.model.TableEntity;
import com.mysql.cj.MysqlConnection;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 17:23
 * @description:
 * @version: 1.0
 */
public class SqlReader {

    String sqlTables = "select table_name,table_comment from information_schema.tables where table_schema = ?" +
            " order by table_name asc";
    String sqlColumns = "select column_name,column_type,column_key,is_nullable,column_comment,column_default from information_schema" +
            ".columns where table_schema = ?  and table_name " +
            "= ? ";

    private DatasourceProperties datasourceProperties;

    private Db db;

    public SqlReader(DatasourceProperties datasourceProperties) {
        this.datasourceProperties = datasourceProperties;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(StrUtil.format("jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai",datasourceProperties.getHost(),datasourceProperties.getPort(),datasourceProperties.getDatabaseName()));
        dataSource.setPassword(datasourceProperties.getPassword());
        dataSource.setUser(datasourceProperties.getUsername());
        this.db = new Db(dataSource);
    }

    public List<TableEntity> getTableData() {
        List<TableEntity> tables = new ArrayList<TableEntity>();
        try {
            List<Entity> query = db.query(sqlTables, datasourceProperties.getDatabaseName());
            for (Entity entity : query) {
                TableEntity tableEntity = new TableEntity();
                tableEntity.setTableName(entity.getStr("table_name"));
                tableEntity.setTableComment(entity.getStr("table_comment"));
                tableEntity.setColumns(getColumnList(tableEntity.getTableName()));
                tables.add(tableEntity);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tables;
    }

    public List<ExcelView> getExcelViewList() {
        return getTableData().stream().map(t -> {
            ExcelView view = new ExcelView(t);
            return view;
        }).collect(Collectors.toList());
    }

    private List<ColumnEntity> getColumnList(String tableName) {
        List<ColumnEntity> list = new ArrayList<ColumnEntity>();
        try {
            List<Entity> query = db.query(sqlColumns, datasourceProperties.getDatabaseName(), tableName);
            for (Entity entity : query) {
                ColumnEntity columnEntity = new ColumnEntity();
                columnEntity.setName(entity.getStr("column_name"));
                columnEntity.setType(entity.getStr("column_type"));
                columnEntity.setKey(entity.getStr("column_key"));
                columnEntity.setIsNullable(entity.getStr("is_nullable").equals("NO") ? "否" : "是");
                columnEntity.setComment(entity.getStr("column_comment"));
                columnEntity.setColumnDefault(entity.getStr("column_default"));
                list.add(columnEntity);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

}
