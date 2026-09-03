package com.zapi.openapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.openapi.entity.OpenApiConfig;

import java.util.List;

/**
 * 开放接口配置Service接口
 */
public interface OpenApiConfigService extends IService<OpenApiConfig> {

    /**
     * 分页查询配置列表
     */
    Page<OpenApiConfig> getConfigPage(Integer current, Integer size, Long appId,
                                       String configName, Integer callType,
                                       String dataType, Integer status);

    /**
     * 根据应用ID获取配置列表
     */
    List<OpenApiConfig> getConfigsByAppId(Long appId);

    /**
     * 根据数据类型获取启用的配置
     */
    List<OpenApiConfig> getEnabledConfigsByDataType(String dataType);

    /**
     * 更新同步时间
     */
    void updateSyncTime(Long configId, String lastSyncValue);

    /**
     * 根据UUID获取配置
     */
    OpenApiConfig getConfigByUuid(String configUuid);
}
