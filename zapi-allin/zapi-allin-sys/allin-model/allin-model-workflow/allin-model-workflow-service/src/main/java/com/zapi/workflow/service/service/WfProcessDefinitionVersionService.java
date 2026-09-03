package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.entity.WfProcessDefinitionVersion;

/**
 * 流程定义版本记录服务接口
 * 提供流程定义版本记录的业务操作方法
 */
public interface WfProcessDefinitionVersionService extends IService<WfProcessDefinitionVersion> {
    
    /**
     * 保存流程定义版本记录
     * 将指定的流程定义信息保存为历史版本记录
     *
     * @param processDefinition 流程定义实体，用于创建版本记录
     * @return 保存后的版本记录实体
     */
    WfProcessDefinitionVersion saveVersionRecord(WfProcessDefinitionVersion processDefinition);
    
    /**
     * 根据流程定义ID获取最新版本记录
     * 获取指定流程定义的最新版本记录
     *
     * @param processDefId 流程定义ID
     * @return 最新版本的流程定义版本记录，如果不存在则返回null
     */
    WfProcessDefinitionVersion getLatestVersionByProcessDefId(String processDefId);
    
    /**
     * 根据流程定义ID和版本号获取版本记录
     * 获取指定流程定义的特定版本记录
     *
     * @param processDefId 流程定义ID
     * @param version 版本号
     * @return 指定版本的流程定义版本记录，如果不存在则返回null
     */
    WfProcessDefinitionVersion getVersionByProcessDefIdAndVersion(String processDefId, Integer version);
    
    /**
     * 根据流程定义ID和内容获取已存在的版本记录
     * 用于检查是否已有相同内容的版本记录，避免重复创建
     *
     * @param processDefId 流程定义ID
     * @param jsonDef 流程定义JSON内容
     * @return 已存在的版本记录，如果不存在则返回null
     */
    WfProcessDefinitionVersion getExistingVersionByContent(String processDefId, String jsonDef);
}