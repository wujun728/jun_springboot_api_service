package com.zapi.openapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.openapi.entity.OpenApiConfig;
import com.zapi.openapi.entity.SyncDataReq;
import com.zapi.openapi.service.OpenApiConfigService;
import com.zapi.openapi.service.SysSyncService;
import com.zapi.openapi.strategy.DataSyncStrategy;
import com.zapi.openapi.strategy.DataSyncStrategyFactory;
import com.zapi.openapi.util.HttpClientUtil;
import com.zapi.workbench.entity.WbApplication;
import com.zapi.workbench.service.WbApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据同步服务实现类
 * 提供全量和增量同步用户、角色、机构的方法
 */
@Slf4j
@Service
public class SysSyncServiceImpl implements SysSyncService {

    @Autowired
    private OpenApiConfigService openApiConfigService;

    @Autowired
    private WbApplicationService wbApplicationService;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private DataSyncStrategyFactory dataSyncStrategyFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据字段映射配置转换数据
     */
    @Override
    public Map<String, Object> transformDataByMapping(Map<String, Object> sourceData, String fieldMappingJson) {
        if (fieldMappingJson == null || fieldMappingJson.trim().isEmpty()) {
            // 如果没有字段映射配置，直接返回原数据
            return sourceData;
        }
        
        try {
            // 解析字段映射JSON
            Map<String, String> fieldMapping = objectMapper.readValue(fieldMappingJson, new TypeReference<Map<String, String>>() {});
            
            // 创建转换后的数据映射
            Map<String, Object> transformedData = new HashMap<>();
            
            // 根据字段映射配置转换数据
            for (Map.Entry<String, String> mappingEntry : fieldMapping.entrySet()) {
                String targetField = mappingEntry.getKey(); // 目标字段名
                String sourceField = mappingEntry.getValue(); // 源字段名
                
                // 从源数据中获取值并映射到目标字段
                Object value = sourceData.get(sourceField);
                transformedData.put(targetField, value);
            }
            
            // 如果源数据中有字段映射中未定义的字段，也保留到转换后的数据中
            for (Map.Entry<String, Object> sourceEntry : sourceData.entrySet()) {
                String sourceField = sourceEntry.getKey();
                // 如果目标字段中还没有这个字段，则保留
                if (!transformedData.containsKey(sourceField)) {
                    transformedData.put(sourceField, sourceEntry.getValue());
                }
            }
            
            return transformedData;
        } catch (JsonProcessingException e) {
            // 如果解析字段映射配置失败，返回原数据
            return sourceData;
        }
    }

    /**
     * 主动调用外部接口获取数据并同步
     */
    @Override
    @Transactional
    public String pullDataFromRemote(Long configId) {
        try {
            // 获取配置信息
            OpenApiConfig config = openApiConfigService.getById(configId);
            if (config == null) {
                return "配置不存在";
            }

            return executePullSync(config);
        } catch (Exception e) {
            log.error("主动同步失败: configId={}, 错误={}", configId, e.getMessage(), e);
            return "主动同步失败: " + e.getMessage();
        }
    }

    /**
     * 执行主动同步逻辑（三步骤流程）
     */
    private String executePullSync(OpenApiConfig config) throws Exception {
        log.info("开始主动同步: configId={}, dataType={}, remoteUrl={}",
                config.getId(), config.getDataType(), config.getRemoteUrl());

        // 第一步：验证配置
        String validationResult = validatePullSyncConfig(config);
        if (validationResult != null) {
            return validationResult;
        }

        // 第二步：调用远程接口获取数据
        Map<String, Object> responseData = fetchRemoteData(config);

        // 第三步：执行数据同步策略
        String syncResult = executeSyncStrategy(config, responseData);

        // 更新最后同步时间
        openApiConfigService.updateSyncTime(config.getId(), LocalDateTime.now().toString());

        log.info("主动同步完成: configId={}, result={}", config.getId(), syncResult);
        return syncResult;
    }

    /**
     * 第一步：验证主动同步配置
     * @return 验证失败时返回错误信息，验证成功返回null
     */
    private String validatePullSyncConfig(OpenApiConfig config) {
        // 验证是否为主动模式
        if (config.getCallType() != 2) {
            return "该配置不是主动模式";
        }

        // 验证配置是否启用
        if (config.getStatus() != 1) {
            return "配置已禁用";
        }

        // 验证远程URL
        if (config.getRemoteUrl() == null || config.getRemoteUrl().trim().isEmpty()) {
            return "远程接口地址未配置";
        }

        // 验证关联应用
        WbApplication app = wbApplicationService.getById(config.getAppId());
        if (app == null) {
            return "关联的应用不存在";
        }

        return null; // 验证通过
    }

    /**
     * 第二步：调用远程接口获取数据
     */
    private Map<String, Object> fetchRemoteData(OpenApiConfig config) throws Exception {
        // 获取应用信息用于鉴权
        WbApplication app = wbApplicationService.getById(config.getAppId());

        // 合并用户配置的请求头和系统鉴权信息
        String requestHeaders = mergeAuthHeaders(config.getRequestHeaders(), app);

        // 准备请求参数（增量同步需添加lastSyncValue）
        String requestParams = prepareRequestParams(config);

        // 调用远程接口
        String responseData = httpClientUtil.sendRequest(
                config.getRemoteMethod(),
                config.getRemoteUrl(),
                requestHeaders,
                requestParams
        );

        log.info("远程接口响应成功: configId={}, responseLength={}",
                config.getId(), responseData != null ? responseData.length() : 0);

        // 解析响应数据
        return objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 准备请求参数（增量同步时添加lastSyncValue）
     */
    private String prepareRequestParams(OpenApiConfig config) throws JsonProcessingException {
        String requestParams = config.getRequestParams();

        // 如果是增量同步，添加lastSyncValue参数
        if (config.getSyncMode() == 2 && config.getLastSyncValue() != null) {
            try {
                Map<String, Object> paramMap = new HashMap<>();
                if (requestParams != null && !requestParams.trim().isEmpty()) {
                    paramMap = objectMapper.readValue(requestParams, new TypeReference<Map<String, Object>>() {});
                }

                // 添加增量同步参数
                if (config.getIncrementField() != null && !config.getIncrementField().isEmpty()) {
                    paramMap.put(config.getIncrementField(), config.getLastSyncValue());
                }

                return objectMapper.writeValueAsString(paramMap);
            } catch (Exception e) {
                log.warn("处理增量同步参数失败，使用原始参数: {}", e.getMessage());
                return requestParams;
            }
        }

        return requestParams;
    }

    /**
     * 第三步：执行数据同步策略
     */
    private String executeSyncStrategy(OpenApiConfig config, Map<String, Object> responseData) {
        // 获取对应的同步策略
        DataSyncStrategy strategy = dataSyncStrategyFactory.getStrategy(config.getDataType());
        if (strategy == null) {
            return "不支持的数据类型: " + config.getDataType();
        }

        // 构建同步请求对象
        SyncDataReq syncDataReq = new SyncDataReq();
        syncDataReq.setAppId(config.getAppId());
        syncDataReq.setDataType(config.getDataType());
        syncDataReq.setConfigUuid(config.getConfigUuid());
        syncDataReq.setData(responseData);
        syncDataReq.setTimestamp(config.getLastSyncValue());

        // 根据同步方式调用相应的策略方法
        if (config.getSyncMode() == 1) {
            // 全量同步
            strategy.fullSync(syncDataReq);
            return config.getDataType() + " 全量同步成功";
        } else {
            // 增量同步
            strategy.incrementalSync(syncDataReq);
            return config.getDataType() + " 增量同步成功";
        }
    }

    /**
     * 合并用户配置的请求头和系统鉴权信息
     */
    private String mergeAuthHeaders(String userHeaders, WbApplication app) {
        try {
            Map<String, String> headerMap = new HashMap<>();
            
            // 解析用户配置的请求头
            if (userHeaders != null && !userHeaders.trim().isEmpty()) {
                Map<String, String> userHeaderMap = objectMapper.readValue(userHeaders,
                        new TypeReference<Map<String, String>>() {});
                headerMap.putAll(userHeaderMap);
            }
            
            // 添加系统鉴权信息（会覆盖用户配置的同名header）
            headerMap.put("App-Id", String.valueOf(app.getId()));
            headerMap.put("X-Auth-Code", app.getAuthCode());
            headerMap.put("X-Auth-Psw", app.getAuthPsw());
            
            return objectMapper.writeValueAsString(headerMap);
        } catch (Exception e) {
            log.warn("合并请求头失败，仅使用鉴权信息: {}", e.getMessage());
            
            // 如果解析失败，只返回鉴权信息
            Map<String, String> authHeaders = new HashMap<>();
            authHeaders.put("App-Id", String.valueOf(app.getId()));
            authHeaders.put("X-Auth-Code", app.getAuthCode());
            authHeaders.put("X-Auth-Psw", app.getAuthPsw());
            
            try {
                return objectMapper.writeValueAsString(authHeaders);
            } catch (JsonProcessingException ex) {
                log.error("生成鉴权请求头失败", ex);
                return "{}";
            }
        }
    }
}