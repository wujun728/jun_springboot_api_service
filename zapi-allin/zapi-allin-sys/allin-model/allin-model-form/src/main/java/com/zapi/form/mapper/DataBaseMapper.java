package com.zapi.form.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/***
 *
 * 数据模型相关
 * @author zhonglz
 * @create 2025年08月19日11:35:17
 */
@Mapper
public interface DataBaseMapper {

    /**
     * 创建数据模型物理主表
     * @param tableName 表名
     * @param tableComment 表描述
     */
    void createMainTable(@Param("tableName") String tableName, @Param("tableComment") String tableComment);


    /**
     * 表名称变更
     * @param tableName 原表名
     * @param newTableName 新表名
     */
    void alterTableName(@Param("tableName") String tableName,@Param("newTableName") String newTableName);

    /**
     * 表删除
     * @param tableName
     */
    void dropTable(@Param("tableName") String tableName);


    /**
     * 查询当前使用数据库名
     * @return
     */
    String selectCurrentDatabase();

    /**
     * 检查表名是否已存在
     * @param tableScheme
     * @param tableName
     * @return
     */
    Integer checkTableNameIsExisted(@Param("tableScheme") String tableScheme,@Param("tableName") String tableName);


    /**
     * 检查表字段是否已存在
     * @param tableScheme
     * @param tableName
     * @return
     */
    Integer checkColumnNameIsExisted(@Param("tableScheme") String tableScheme,@Param("tableName") String tableName,@Param("columnName") String columnName);

    /**
     * 批量新增表字段
     * @param tableName 表名
     * @param columns 字段信息列表
     */
    void batchAddColumns(@Param("tableName") String tableName, @Param("columns") List<Map<String, Object>> columns);

    /**
     * 批量修改表字段
     * @param tableName 表名
     * @param columns 字段信息列表
     */
    void batchModifyColumns(@Param("tableName") String tableName, @Param("columns") List<Map<String, Object>> columns);

    /**
     * 批量删除表字段
     * @param tableName 表名
     * @param columnNames 要删除的字段名列表
     */
    void batchDropColumns(@Param("tableName") String tableName, @Param("columnNames") List<String> columnNames);

    /**
     * 执行自定义 DDL 语句
     * @param ddlSql DDL SQL 语句
     */
    void executeDDL(@Param("ddlSql") String ddlSql);

    /**
     * 批量执行 DDL 语句
     * @param ddlSqlList DDL SQL 语句列表
     */
    void batchExecuteDDL(@Param("ddlSqlList") List<String> ddlSqlList);

    /**
     * 新增单个字段
     * @param tableName 表名
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param columnLength 字段长度
     * @param isNullable 是否可为空
     * @param defaultValue 默认值
     * @param columnComment 字段注释
     */
    void addColumn(@Param("tableName") String tableName, 
                   @Param("columnName") String columnName, 
                   @Param("columnType") String columnType, 
                   @Param("columnLength") Integer columnLength, 
                   @Param("isNullable") Boolean isNullable, 
                   @Param("defaultValue") String defaultValue, 
                   @Param("columnComment") String columnComment);

    /**
     * 修改单个字段
     * @param tableName 表名
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param columnLength 字段长度
     * @param isNullable 是否可为空
     * @param defaultValue 默认值
     * @param columnComment 字段注释
     */
    void modifyColumn(@Param("tableName") String tableName, 
                      @Param("columnName") String columnName, 
                      @Param("columnType") String columnType, 
                      @Param("columnLength") Integer columnLength, 
                      @Param("isNullable") Boolean isNullable, 
                      @Param("defaultValue") String defaultValue, 
                      @Param("columnComment") String columnComment);

    /**
     * 删除单个字段
     * @param tableName 表名
     * @param columnName 字段名
     */
    void dropColumn(@Param("tableName") String tableName, @Param("columnName") String columnName);
}
