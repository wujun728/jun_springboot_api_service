package com.zapi.form.service;


import com.zapi.form.model.dto.FormColumnInfoDTO;

import java.util.List;

/****
 * 数据库操作模型相关接口
 * @author zhonglz
 * @date 2025年08月19日11:54:16
 */
public interface DataBaseService {


    /***
     * 创建业务主表
     * @param formId 表单ID
     * @param tableName 表名
     * @param tableComment 表描述
     */
    void createMainTable(String formId,String tableName,String tableComment);


    /****
     * 添加业务表系统字段
     * @param formId
     */
    void addSystemColumns(String formId);

    /**
     * 表名称变更
     * @param tableName 原表名
     * @param newTableName 新表名
     */
    void alterTableName(String tableName, String newTableName);

    /**
     * 表删除
     * @param tableName
     */
    void dropTable( String tableName);

    /**
     * 执行自定义 DDL 语句
     * @param ddlSql DDL SQL 语句
     */
    void executeDDL(String ddlSql);

    /**
     * 批量执行 DDL 语句
     * @param ddlSqlList DDL SQL 语句列表
     */
    void batchExecuteDDL(List<String> ddlSqlList);

    /**
     * 批量新增表字段（使用 ColumnInfoDTO）
     * @param tableName 表名
     * @param columns 字段信息列表
     */
    void batchAddColumnsWithDTO(String tableName, List<FormColumnInfoDTO> columns);

    /**
     * 批量修改表字段（使用 ColumnInfoDTO）
     * @param tableName 表名
     * @param columns 字段信息列表
     */
    void batchModifyColumnsWithDTO(String tableName, List<FormColumnInfoDTO> columns);

    /**
     * 检查字段是否存在，如果存在则忽略，不存在则新增（使用 ColumnInfoDTO）
     * @param tableName 表名
     * @param columns 字段信息列表
     */
    void addColumnsIfNotExistsWithDTO(String tableName, List<FormColumnInfoDTO> columns);

    /**
     * 新增单个字段（使用 ColumnInfoDTO）
     * @param tableName 表名
     * @param column 字段信息
     */
    void addColumnWithDTO(String tableName, FormColumnInfoDTO column);

    /**
     * 修改单个字段（使用 ColumnInfoDTO）
     * @param tableName 表名
     * @param column 字段信息
     */
    void modifyColumnWithDTO(String tableName, FormColumnInfoDTO column);

    /**
     * 获取当前数据库名
     * @return 当前数据库名
     */
    String getCurrentDatabase();

    /**
     * 检查表是否存在
     * @param databaseName 数据库名
     * @param tableName 表名
     * @return 1表示存在，0表示不存在
     */
    Integer checkTableExists(String databaseName, String tableName);
}
