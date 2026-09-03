package com.zapi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.form.mapper.FormTableInfoMapper;
import com.zapi.form.model.entity.FormTableInfo;
import com.zapi.form.service.FormTableInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库模型数据表 服务实现类
 * 
 * @author zhonglz
 * @date 2025年08月19日11:35:17
 */
@Service
public class FormTableInfoServiceImpl extends ServiceImpl<FormTableInfoMapper, FormTableInfo> implements FormTableInfoService {

    @Override
    public FormTableInfo getByFormId(String formId) {
        return this.baseMapper.selectByFormId(formId);
    }

    @Override
    public FormTableInfo getByTableName(String tableName) {
        return baseMapper.selectByTableName(tableName);
    }

    @Override
    public boolean deleteByFormId(String formId) {
        return baseMapper.deleteByFormId(formId) > 0;
    }

    @Override
    public boolean deleteByFormIds(List<String> formIds) {
        return baseMapper.deleteByFormIds(formIds) > 0;
    }

    @Override
    public boolean isTableNameExists(String tableName) {
        return baseMapper.checkTableNameExists(tableName) > 0;
    }

    @Override
    public boolean saveOrUpdateTableInfo(FormTableInfo tableInfo) {
        if (tableInfo.getId() != null) {
            // 更新
            return updateById(tableInfo);
        } else {
            // 新增
            return save(tableInfo);
        }
    }

    @Override
    public List<FormTableInfo> listByFormIds(List<String> formIds) {
        LambdaQueryWrapper<FormTableInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(FormTableInfo::getFormId, formIds);
        return list(wrapper);
    }
} 