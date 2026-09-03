package com.zapi.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.openapi.entity.SyncConfig;
import com.zapi.openapi.service.SyncConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 数据同步配置控制器
 * 用于管理字段映射配置
 */
@Api(tags = "开放接口-同步配置管理")
@RestController
@RequestMapping("/openapi/sync-config")
public class SyncConfigController {

    @Autowired
    private SyncConfigService syncConfigService;

    /**
     * 分页查询同步配置列表
     */
    @ApiOperation("分页查询同步配置列表")
    @GetMapping("/page")
    public Result<Page<SyncConfig>> page(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("应用ID") @RequestParam(required = false) Long appId,
            @ApiParam("同步类型") @RequestParam(required = false) String syncType,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        
        Page<SyncConfig> page = new Page<>(current, size);
        QueryWrapper<SyncConfig> queryWrapper = new QueryWrapper<>();
        
        if (appId != null) {
            queryWrapper.eq("app_id", appId);
        }
        if (syncType != null) {
            queryWrapper.eq("sync_type", syncType);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.eq("deleted", 0).orderByDesc("create_time");
        Page<SyncConfig> result = syncConfigService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 根据ID获取同步配置
     */
    @ApiOperation("根据ID获取同步配置")
    @GetMapping("/{id}")
    public Result<SyncConfig> getById(@ApiParam("配置ID") @PathVariable Long id) {
        SyncConfig config = syncConfigService.getById(id);
        if (config != null && config.getDeleted() == 0) {
            return Result.success(config);
        }
        return Result.error("配置不存在");
    }

    /**
     * 新增同步配置
     */
    @ApiOperation("新增同步配置")
    @PostMapping
    public Result<String> add(@RequestBody SyncConfig config) {
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        config.setDeleted(0);
        
        boolean result = syncConfigService.save(config);
        if (result) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    /**
     * 更新同步配置
     */
    @ApiOperation("更新同步配置")
    @PutMapping
    public Result<String> update(@RequestBody SyncConfig config) {
        SyncConfig existing = syncConfigService.getById(config.getId());
        if (existing == null || existing.getDeleted() == 1) {
            return Result.error("配置不存在");
        }
        
        existing.setAppId(config.getAppId());
        existing.setSyncType(config.getSyncType());
        existing.setFieldMapping(config.getFieldMapping());
        existing.setStatus(config.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        
        boolean result = syncConfigService.updateById(existing);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除同步配置
     */
    @ApiOperation("删除同步配置")
    @DeleteMapping("/{id}")
    public Result<String> delete(@ApiParam("配置ID") @PathVariable Long id) {
        SyncConfig config = syncConfigService.getById(id);
        if (config == null || config.getDeleted() == 1) {
            return Result.error("配置不存在");
        }
        
        config.setDeleted(1);
        config.setUpdateTime(LocalDateTime.now());
        
        boolean result = syncConfigService.updateById(config);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 根据应用ID和同步类型获取配置
     */
    @ApiOperation("根据应用ID和同步类型获取配置")
    @GetMapping("/by-app-and-type")
    public Result<SyncConfig> getConfigByAppIdAndType(
            @ApiParam("应用ID") @RequestParam Long appId,
            @ApiParam("同步类型") @RequestParam String syncType) {
        
        SyncConfig config = syncConfigService.getConfigByAppIdAndType(appId, syncType);
        if (config != null) {
            return Result.success(config);
        }
        return Result.error("未找到对应配置");
    }
}