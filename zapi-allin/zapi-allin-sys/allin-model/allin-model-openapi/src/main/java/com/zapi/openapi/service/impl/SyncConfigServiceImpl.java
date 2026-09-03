package com.zapi.openapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.openapi.entity.SyncConfig;
import com.zapi.openapi.mapper.SyncConfigMapper;
import com.zapi.openapi.service.SyncConfigService;
import org.springframework.stereotype.Service;

@Service
public class SyncConfigServiceImpl extends ServiceImpl<SyncConfigMapper, SyncConfig> implements SyncConfigService {

    @Override
    public SyncConfig getConfigByAppIdAndType(Long appId, String syncType) {
        QueryWrapper<SyncConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId)
                .eq("sync_type", syncType)
                .eq("status", 1)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        
        return getOne(queryWrapper);
    }
}