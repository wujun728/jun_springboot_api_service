package com.zapi.openapi.controller;

import com.zapi.common.Result;
import com.zapi.openapi.annotation.RequireAppAuth;
import com.zapi.openapi.entity.OpenApiConfig;
import com.zapi.openapi.entity.SyncDataReq;
import com.zapi.openapi.service.OpenApiConfigService;
import com.zapi.openapi.service.SysSyncService;
import com.zapi.openapi.strategy.DataSyncStrategy;
import com.zapi.openapi.strategy.DataSyncStrategyFactory;
import com.zapi.workbench.entity.WbApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据同步Controller
 * 提供全量和增量同步用户、角色、机构的接口
 */
@Api(tags = "开放接口-数据同步")
@RestController
@RequestMapping("/openapi/sync")
public class SyncController {

    @Autowired
    private SysSyncService sysSyncService;

    @Autowired
    private OpenApiConfigService openApiConfigService;

    @Autowired
    private DataSyncStrategyFactory strategyFactory;

    /**
     * 被动模式 - 通过UUID同步数据（用户/角色/部门）
     */
    @ApiOperation("被动模式-通过UUID同步数据")
    @RequireAppAuth
    @RequestMapping(value = "/{dataType}/{configUuid}", method = {RequestMethod.GET, RequestMethod.POST})
    @PermitAll
    public Result<String> syncByUuid(
            @ApiParam("数据类型") @PathVariable String dataType,
            @ApiParam("配置UUID") @PathVariable String configUuid,
            @ApiParam("数据") @RequestBody Map<String, Object> data,
            @ApiParam("时间戳-增量同步") @RequestParam(required = false) String timestamp,
            HttpServletRequest request) {
        try {
            // 从请求中获取已验证的应用和配置信息（由拦截器设置）
            WbApplication app = (WbApplication) request.getAttribute("currentApp");
            OpenApiConfig config = (OpenApiConfig) request.getAttribute("currentConfig");
            
            if (app == null || config == null) {
                return Result.error("应用或配置信息获取失败");
            }

            // 构建同步请求对象
            SyncDataReq syncRequest = new SyncDataReq();
            syncRequest.setDataType(dataType);
            syncRequest.setConfigUuid(configUuid);
            syncRequest.setData(data);
            syncRequest.setTimestamp(timestamp);
            syncRequest.setAppId(app.getId());
            syncRequest.setConfig(config);
            syncRequest.setApplication(app);
            
            // 根据数据类型获取对应的策略
            DataSyncStrategy strategy = strategyFactory.getStrategy(dataType);
            
            // 根据同步模式执行对应的方法
            String result;
            if (config.getSyncMode() == 1) {
                // 全量同步
                result = strategy.fullSync(syncRequest);
            } else {
                // 增量同步
                result = strategy.incrementalSync(syncRequest);
            }
            
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("数据同步失败: " + e.getMessage());
        }
    }

    /**
     * 主动模式 - 手动触发同步
     */
    @ApiOperation("主动模式-手动触发同步")
    @PostMapping("/pull/{configId}")
    @PermitAll
    public Result<String> pullSync(@ApiParam("配置ID") @PathVariable Long configId) {
        try {
            String result = sysSyncService.pullDataFromRemote(configId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("主动同步失败: " + e.getMessage());
        }
    }
    /**
     * 测试接口配置
     */
    @ApiOperation("测试接口配置")
    @PostMapping("/test/{configId}")
    public Result<Map<String, Object>> testConfig(@ApiParam("配置ID") @PathVariable Long configId) {
        try {
            OpenApiConfig config = openApiConfigService.getById(configId);
            if (config == null) {
                return Result.error("配置不存在");
            }

            Map<String, Object> testResult = new HashMap<>();
            testResult.put("configId", config.getId());
            testResult.put("configName", config.getConfigName());
            testResult.put("callType", config.getCallType());
            testResult.put("dataType", config.getDataType());
            testResult.put("syncMode", config.getSyncMode());

            if (config.getCallType() == 1) {
                // 被动模式
                testResult.put("apiPath", config.getApiPath());
                testResult.put("apiMethod", config.getApiMethod());
                testResult.put("fullUrl", "待客户端调用: " + config.getApiPath());
                testResult.put("status", "被动模式配置正常，等待外部调用");
            } else {
                // 主动模式 - 测试远程连接
                testResult.put("remoteUrl", config.getRemoteUrl());
                testResult.put("remoteMethod", config.getRemoteMethod());
                
                try {
                    // 尝试调用远程接口
                    String response = sysSyncService.pullDataFromRemote(configId);
                    testResult.put("status", "success");
                    testResult.put("message", "远程接口调用成功");
                    testResult.put("syncResult", response);
                } catch (Exception e) {
                    testResult.put("status", "failed");
                    testResult.put("message", "远程接口调用失败: " + e.getMessage());
                }
            }

            return Result.success(testResult);
        } catch (Exception e) {
            return Result.error("测试失败: " + e.getMessage());
        }
    }
}