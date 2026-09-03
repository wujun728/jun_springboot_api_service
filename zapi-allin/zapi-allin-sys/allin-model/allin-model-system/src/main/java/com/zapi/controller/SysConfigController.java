package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.SysConfig;
import com.zapi.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 参数配置Controller
 */
@Api(tags = "参数配置管理")
@RestController
@RequestMapping("/system/config")
public class SysConfigController {

    @Autowired
    private SysConfigService configService;

    /**
     * 分页查询参数配置列表
     */
    @ApiOperation("分页查询参数配置列表")
    @GetMapping("/list")
    public Result<Page<SysConfig>> list(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("参数名称") @RequestParam(required = false) String configName,
            @ApiParam("参数键名") @RequestParam(required = false) String configKey,
            @ApiParam("系统内置") @RequestParam(required = false) String configType) {
        
        Page<SysConfig> page = new Page<>(current, size);
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(configName)) {
            queryWrapper.like(SysConfig::getConfigName, configName);
        }
        if (StringUtils.hasText(configKey)) {
            queryWrapper.like(SysConfig::getConfigKey, configKey);
        }
        if (StringUtils.hasText(configType)) {
            queryWrapper.eq(SysConfig::getConfigType, configType);
        }
        
        queryWrapper.orderByAsc(SysConfig::getConfigId);
        Page<SysConfig> result = configService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 根据ID获取参数配置详情
     */
    @ApiOperation("根据ID获取参数配置详情")
    @GetMapping("/{configId}")
    public Result<SysConfig> getInfo(@ApiParam("参数ID") @PathVariable Integer configId) {
        SysConfig config = configService.getById(configId);
        if (config != null) {
            return Result.success(config);
        }
        return Result.error("参数配置不存在");
    }

    /**
     * 根据参数键名查询参数配置
     */
    @ApiOperation("根据参数键名查询参数配置")
    @GetMapping("/key/{configKey}")
    public Result<SysConfig> getByKey(@ApiParam("参数键名") @PathVariable String configKey) {
        SysConfig config = configService.selectConfigByKey(configKey);
        if (config != null) {
            return Result.success(config);
        }
        return Result.error("参数配置不存在");
    }

    /**
     * 根据参数键名获取参数键值
     */
    @ApiOperation("根据参数键名获取参数键值")
    @GetMapping("/value/{configKey}")
    public Result<String> getValueByKey(@ApiParam("参数键名") @PathVariable String configKey) {
        String configValue = configService.selectConfigValueByKey(configKey);
        if (configValue != null) {
            return Result.success(configValue);
        }
        return Result.error("参数配置不存在");
    }

    /**
     * 新增参数配置
     */
    @ApiOperation("新增参数配置")
    @PostMapping("/add")
    public Result<String> add(@RequestBody SysConfig config) {
        // 校验参数键名是否唯一
        if (!configService.checkConfigKeyUnique(config)) {
            return Result.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        
        // 设置默认值
        if (!StringUtils.hasText(config.getConfigType())) {
            config.setConfigType("N");
        }
        
        boolean success = configService.save(config);
        if (success) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    /**
     * 修改参数配置
     */
    @ApiOperation("修改参数配置")
    @PutMapping("/update")
    public Result<String> update(@RequestBody SysConfig config) {
        // 校验参数键名是否唯一
        if (!configService.checkConfigKeyUnique(config)) {
            return Result.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        
        boolean success = configService.updateById(config);
        if (success) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除参数配置
     */
    @ApiOperation("删除参数配置")
    @DeleteMapping("/delete/{configIds}")
    public Result<String> delete(@ApiParam("参数ID，多个用逗号分隔") @PathVariable Integer[] configIds) {
        // 检查是否包含系统内置参数
        for (Integer configId : configIds) {
            SysConfig config = configService.getById(configId);
            if (config != null && "Y".equals(config.getConfigType())) {
                return Result.error("系统内置参数'" + config.getConfigName() + "'不允许删除");
            }
        }
        
        boolean success = configService.removeByIds(Arrays.asList(configIds));
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 刷新参数缓存
     */
    @ApiOperation("刷新参数缓存")
    @DeleteMapping("/refreshCache")
    public Result<String> refreshCache() {
        // 这里可以添加缓存刷新逻辑
        return Result.success("刷新成功");
    }
}