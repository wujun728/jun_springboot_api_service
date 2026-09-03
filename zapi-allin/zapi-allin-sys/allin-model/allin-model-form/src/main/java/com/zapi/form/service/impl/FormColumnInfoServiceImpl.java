package com.zapi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.form.mapper.FormColumnInfoMapper;
import com.zapi.form.model.dto.FormColumnInfoDTO;
import com.zapi.form.model.entity.FormColumnInfo;
import com.zapi.form.service.FormColumnInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库模型数据表字段 服务实现类
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
@Service
public class FormColumnInfoServiceImpl extends ServiceImpl<FormColumnInfoMapper, FormColumnInfo> implements FormColumnInfoService {


    @Override
    public boolean batchSave(List<FormColumnInfo> columns) {
        if (columns == null || columns.isEmpty()) {
            return true;
        }
        for (FormColumnInfo column : columns) {
            if (column.getId() == null) {
                column.setId(IdWorker.getId());
            }
        }
        return this.baseMapper.batchInsert(columns) > 0;
    }

    @Override
    public boolean addColumnsToDeleteAll(String formId, List<FormColumnInfo> columns) {
        if (formId == null) {
            return false;
        }
        
        // 1. 先删除所有非系统字段
        this.baseMapper.deleteNonSystemColumnsByFormId(formId);
        
        // 2. 如果columns为空，只删除不新增
        if (columns == null || columns.isEmpty()) {
            return true;
        }
        // 3. 为所有字段设置formId以及业务字段
        for (FormColumnInfo column : columns) {
            column.setFormId(formId);
            column.setIsSysField(0);
            if (column.getId() == null) {
                column.setId(IdWorker.getId());
            }
        }
        
        // 4. 批量新增字段
        return baseMapper.batchInsert(columns) > 0;
    }

    @Override
    public boolean addColumnsToDeleteAllWithDTO(String formId, List<FormColumnInfoDTO> columnDTOs) {
        if (formId == null) {
            return false;
        }
        
        // 1. 先删除所有非系统字段
        baseMapper.deleteNonSystemColumnsByFormId(formId);
        
        // 2. 如果columnDTOs为空，只删除不新增
        if (columnDTOs == null || columnDTOs.isEmpty()) {
            return true;
        }
        
        // 3. 将DTO转换为实体对象
        List<FormColumnInfo> columns = columnDTOs.stream().map(dto -> {
            FormColumnInfo column = new FormColumnInfo();
            column.setId(IdWorker.getId());
            column.setFormId(formId);
            column.setColumnName(dto.getColumnName());
            column.setColumnType(dto.getColumnType());
            column.setFormType(dto.getFormType());
            column.setColumnComment(dto.getColumnComment());
            column.setColumnLength(dto.getColumnLength());
            column.setColumnScale(dto.getColumnScale());
            column.setIsNullable(dto.getIsNullable());
            column.setDefaultValue(dto.getDefaultValue());
            column.setIsPrimaryKey(dto.getIsPrimaryKey());
            column.setIsAutoIncrement(dto.getIsAutoIncrement());
            column.setCharacterSet(dto.getCharacterSet());
            column.setCollation(dto.getCollation());
            column.setExtra(dto.getExtra());
            column.setIsSysField(0);
            return column;
        }).collect(Collectors.toList());
        
        // 4. 批量新增字段
        return baseMapper.batchInsert(columns) > 0;
    }

    @Override
    public List<FormColumnInfo> listByFormId(String formId) {
        LambdaQueryWrapper<FormColumnInfo> qw = Wrappers.lambdaQuery();
        qw.eq(FormColumnInfo::getFormId, formId).orderByAsc(FormColumnInfo::getId);
        return list(qw);
    }
} 