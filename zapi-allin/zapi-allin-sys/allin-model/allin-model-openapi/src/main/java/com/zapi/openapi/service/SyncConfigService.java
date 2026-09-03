package com.zapi.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.openapi.entity.SyncConfig;

public interface SyncConfigService extends IService<SyncConfig> {
    
    /**
     * 根据应用ID和同步类型获取字段映射配置
     */
    SyncConfig getConfigByAppIdAndType(Long appId, String syncType);
}