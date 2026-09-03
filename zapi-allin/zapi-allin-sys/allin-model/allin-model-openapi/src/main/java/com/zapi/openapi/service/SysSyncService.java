package com.zapi.openapi.service;

import java.util.Map;

/**
 * 数据同步服务接口
 */
public interface SysSyncService {

    /**
     * 根据字段映射配置转换数据
     */
    Map<String, Object> transformDataByMapping(Map<String, Object> sourceData, String fieldMappingJson);

    /**
     * 主动调用外部接口获取数据并同步
     * @param configId 配置ID
     * @return 同步结果
     */
    String pullDataFromRemote(Long configId);

}