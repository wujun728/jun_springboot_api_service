package com.zapi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.form.model.entity.FormTableInfo;

import java.util.List;

/**
 * 数据库模型数据表 服务接口
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
public interface FormTableInfoService extends IService<FormTableInfo> {

    /**
     * 根据表单ID查询表信息
     * @param formId 表单ID
     * @return 表信息
     */
    FormTableInfo getByFormId(String formId);

    /**
     * 根据表名查询表信息
     * @param tableName 表名
     * @return 表信息
     */
    FormTableInfo getByTableName(String tableName);

    /**
     * 根据表单ID删除表信息
     * @param formId 表单ID
     * @return 是否删除成功
     */
    boolean deleteByFormId(String formId);

    /**
     * 批量删除表信息
     * @param formIds 表单ID列表
     * @return 是否删除成功
     */
    boolean deleteByFormIds(List<String> formIds);

    /**
     * 检查表名是否存在
     * @param tableName 表名
     * @return 是否存在
     */
    boolean isTableNameExists(String tableName);

    /**
     * 保存或更新表信息
     * @param tableInfo 表信息
     * @return 是否保存成功
     */
    boolean saveOrUpdateTableInfo(FormTableInfo tableInfo);

    /**
     * 根据表单ID列表查询表信息列表
     * @param formIds 表单ID列表
     * @return 表信息列表
     */
    List<FormTableInfo> listByFormIds(List<String> formIds);
} 