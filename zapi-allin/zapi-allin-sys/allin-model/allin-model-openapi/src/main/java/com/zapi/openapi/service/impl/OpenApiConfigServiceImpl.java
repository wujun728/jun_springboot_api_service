package com.zapi.openapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.openapi.entity.OpenApiConfig;
import com.zapi.openapi.mapper.OpenApiConfigMapper;
import com.zapi.openapi.service.OpenApiConfigService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 开放接口配置Service实现类
 */
@Service
public class OpenApiConfigServiceImpl extends ServiceImpl<OpenApiConfigMapper, OpenApiConfig> implements OpenApiConfigService {

    @Override
    public Page<OpenApiConfig> getConfigPage(Integer current, Integer size, Long appId,
                                              String configName, Integer callType,
                                              String dataType, Integer status) {
        Page<OpenApiConfig> page = new Page<>(current, size);
        return baseMapper.selectPageWithAppName(page, appId, configName, callType, dataType, status);
    }

    @Override
    public List<OpenApiConfig> getConfigsByAppId(Long appId) {
        QueryWrapper<OpenApiConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId).orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public List<OpenApiConfig> getEnabledConfigsByDataType(String dataType) {
        QueryWrapper<OpenApiConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_type", dataType)
                    .eq("status", 1)
                    .orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public void updateSyncTime(Long configId, String lastSyncValue) {
        OpenApiConfig config = new OpenApiConfig();
        config.setId(configId);
        config.setLastSyncTime(LocalDateTime.now());
        config.setLastSyncValue(lastSyncValue);
        updateById(config);
    }

    @Override
    public OpenApiConfig getConfigByUuid(String configUuid) {
        QueryWrapper<OpenApiConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_uuid", configUuid)
                    .eq("status", 1);
        return getOne(queryWrapper);
    }
}
