package com.zapi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.form.model.entity.FormTableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库模型数据表 Mapper 接口
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
@Mapper
public interface FormTableInfoMapper extends BaseMapper<FormTableInfo> {

    /**
     * 根据表单ID查询表信息
     * @param formId 表单ID
     * @return 表信息
     */
    FormTableInfo selectByFormId(@Param("formId") String formId);

    /**
     * 根据表名查询表信息
     * @param tableName 表名
     * @return 表信息
     */
    FormTableInfo selectByTableName(@Param("tableName") String tableName);

    /**
     * 根据表单ID删除表信息
     * @param formId 表单ID
     * @return 删除数量
     */
    int deleteByFormId(@Param("formId") String formId);

    /**
     * 批量删除表信息
     * @param formIds 表单ID列表
     * @return 删除数量
     */
    int deleteByFormIds(@Param("formIds") List<String> formIds);

    /**
     * 检查表名是否存在
     * @param tableName 表名
     * @return 存在数量
     */
    int checkTableNameExists(@Param("tableName") String tableName);
} 