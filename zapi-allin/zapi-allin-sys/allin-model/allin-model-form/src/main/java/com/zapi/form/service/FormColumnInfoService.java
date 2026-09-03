package com.zapi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.form.model.dto.FormColumnInfoDTO;
import com.zapi.form.model.entity.FormColumnInfo;

import java.util.List;

/**
 * 数据库模型数据表字段 服务接口
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
public interface FormColumnInfoService extends IService<FormColumnInfo> {

    /**
     * 批量保存字段信息
     * @param columns 字段列表
     * @return 是否保存成功
     */
    boolean batchSave(List<FormColumnInfo> columns);

    /**
     * 根据表单ID删除所有非系统字段，然后新增字段
     * @param formId 表单ID
     * @param columns 要新增的字段列表
     * @return 是否操作成功
     */
    boolean addColumnsToDeleteAll(String formId, List<FormColumnInfo> columns);

    /**
     * 根据表单ID删除所有非系统字段，然后新增字段（使用DTO）
     * @param formId 表单ID
     * @param columnDTOs 要新增的字段DTO列表
     * @return 是否操作成功
     */
    boolean addColumnsToDeleteAllWithDTO(String formId, List<FormColumnInfoDTO> columnDTOs);

    /**
     * 根据表单ID查询所有字段（含系统、业务字段）
     * @param formId 表单ID
     * @return 字段列表
     */
    List<FormColumnInfo> listByFormId(String formId);
} 