package com.zapi.openapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.openapi.entity.OpenApiConfig;
import com.zapi.openapi.service.OpenApiConfigService;
import com.zapi.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 开放接口配置Controller
 */
@Api(tags = "开放接口-接口配置")
@RestController
@RequestMapping("/openapi/config")
public class OpenApiConfigController {

    @Autowired
    private OpenApiConfigService openApiConfigService;

    /**
     * 分页查询配置列表
     */
    @ApiOperation("分页查询配置列表")
    @GetMapping("/list")
    public Result<Page<OpenApiConfig>> getConfigList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("应用ID") @RequestParam(required = false) Long appId,
            @ApiParam("配置名称") @RequestParam(required = false) String configName,
            @ApiParam("调用类型") @RequestParam(required = false) Integer callType,
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        
        Page<OpenApiConfig> result = openApiConfigService.getConfigPage(
                current, size, appId, configName, callType, dataType, status);
        return Result.success(result);
    }

    /**
     * 根据应用ID获取配置列表
     */
    @ApiOperation("根据应用ID获取配置列表")
    @GetMapping("/byApp/{appId}")
    public Result<List<OpenApiConfig>> getConfigsByAppId(@PathVariable Long appId) {
        List<OpenApiConfig> configs = openApiConfigService.getConfigsByAppId(appId);
        return Result.success(configs);
    }

    /**
     * 根据ID获取配置详情
     */
    @ApiOperation("根据ID获取配置详情")
    @GetMapping("/{id}")
    public Result<OpenApiConfig> getConfigById(@PathVariable Long id) {
        OpenApiConfig config = openApiConfigService.getById(id);
        if (config != null) {
            return Result.success(config);
        }
        return Result.error("配置不存在");
    }

    /**
     * 添加配置
     */
    @ApiOperation("添加配置")
    @PostMapping("/add")
    public Result<OpenApiConfig> addConfig(@RequestBody OpenApiConfig config) {
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        
        // 生成UUID
        String configUuid = UUID.randomUUID().toString().replace("-", "");
        config.setConfigUuid(configUuid);
        
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId != null) {
                config.setCreator(String.valueOf(userId));
            }
        } catch (Exception e) {
            // 忽略
        }
        
        // 被动模式：强制使用POST方法，并生成接口路径
        if (config.getCallType() != null && config.getCallType() == 1) {
            config.setApiMethod("POST");
            config.setApiPath("/openapi/sync/" + config.getDataType() + "/" + configUuid);
        }
        
        boolean success = openApiConfigService.save(config);
        if (success) {
            return Result.success("添加成功", config);
        }
        return Result.error("添加失败");
    }

    /**
     * 更新配置
     */
    @ApiOperation("更新配置")
    @PutMapping("/update")
    public Result<String> updateConfig(@RequestBody OpenApiConfig config) {
        if (config.getId() == null) {
            return Result.error("配置ID不能为空");
        }
        
        OpenApiConfig existing = openApiConfigService.getById(config.getId());
        if (existing == null) {
            return Result.error("配置不存在");
        }
        
        config.setUpdateTime(LocalDateTime.now());
        
        // 被动模式：强制使用POST方法，并更新apiPath
        if (config.getCallType() != null && config.getCallType() == 1) {
            config.setApiMethod("POST");
            // 保持原有UUID，更新路径格式为正确的格式
            config.setApiPath("/openapi/sync/" + config.getDataType() + "/" + existing.getConfigUuid());
        }
        
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId != null) {
                config.setUpdater(String.valueOf(userId));
            }
        } catch (Exception e) {
            // 忽略
        }
        
        boolean success = openApiConfigService.updateById(config);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除配置
     */
    @ApiOperation("删除配置")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteConfig(@PathVariable Long id) {
        boolean success = openApiConfigService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 切换配置状态
     */
    @ApiOperation("切换配置状态")
    @PutMapping("/status/{id}")
    public Result<String> changeStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        
        OpenApiConfig config = openApiConfigService.getById(id);
        if (config == null) {
            return Result.error("配置不存在");
        }
        
        config.setStatus(status);
        config.setUpdateTime(LocalDateTime.now());
        
        boolean success = openApiConfigService.updateById(config);
        if (success) {
            return Result.success(status == 1 ? "启用成功" : "禁用成功");
        }
        return Result.error("操作失败");
    }
}
