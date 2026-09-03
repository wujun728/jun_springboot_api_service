package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.workflow.service.mapper.WfProcessDefinitionVersionMapper;
import com.zapi.workflow.service.model.entity.WfProcessDefinitionVersion;
import com.zapi.workflow.service.service.WfProcessDefinitionVersionService;
import org.springframework.stereotype.Service;

/**
 * 流程定义版本记录服务实现类
 * 实现流程定义版本记录的业务操作方法
 */
@Service
public class WfProcessDefinitionVersionServiceImpl 
    extends ServiceImpl<WfProcessDefinitionVersionMapper, WfProcessDefinitionVersion> 
    implements WfProcessDefinitionVersionService {

    @Override
    public WfProcessDefinitionVersion saveVersionRecord(WfProcessDefinitionVersion processDefinition) {
        // 保存流程定义版本记录
        this.save(processDefinition);
        return processDefinition;
    }

    @Override
    public WfProcessDefinitionVersion getLatestVersionByProcessDefId(String processDefId) {
        // 查询指定流程定义ID的最新版本记录（按版本号降序排列，取第一条）
        LambdaQueryWrapper<WfProcessDefinitionVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfProcessDefinitionVersion::getProcessDefId, processDefId);
        queryWrapper.orderByDesc(WfProcessDefinitionVersion::getVersion);
        
        return this.getOne(queryWrapper, false); // 使用getOne方法，不抛出异常
    }

    @Override
    public WfProcessDefinitionVersion getVersionByProcessDefIdAndVersion(String processDefId, Integer version) {
        // 查询指定流程定义ID和版本号的版本记录
        LambdaQueryWrapper<WfProcessDefinitionVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfProcessDefinitionVersion::getProcessDefId, processDefId);
        queryWrapper.eq(WfProcessDefinitionVersion::getVersion, version);
        
        return this.getOne(queryWrapper, false); // 使用getOne方法，不抛出异常
    }
    
    @Override
    public WfProcessDefinitionVersion getExistingVersionByContent(String processDefId, String jsonDef) {
        // 查询指定流程定义ID和JSON内容的版本记录，用于避免重复创建
        LambdaQueryWrapper<WfProcessDefinitionVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfProcessDefinitionVersion::getProcessDefId, processDefId);
        queryWrapper.eq(WfProcessDefinitionVersion::getJsonDef, jsonDef);
        
        return this.getOne(queryWrapper, false); // 使用getOne方法，不抛出异常
    }
}