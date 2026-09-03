package com.zapi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.form.model.entity.FormColumnInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库模型数据表字段 Mapper 接口
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
@Mapper
public interface FormColumnInfoMapper extends BaseMapper<FormColumnInfo> {

    /**
     * 批量插入字段信息
     * @param columns 字段列表
     * @return 插入数量
     */
    int batchInsert(@Param("columns") List<FormColumnInfo> columns);

    /**
     * 根据表单ID删除所有非系统字段
     * @param formId 表单ID
     * @return 删除数量
     */
    int deleteNonSystemColumnsByFormId(@Param("formId") String formId);


} 