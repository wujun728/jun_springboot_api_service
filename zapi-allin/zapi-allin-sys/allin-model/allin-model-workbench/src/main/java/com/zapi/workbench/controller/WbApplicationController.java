package com.zapi.workbench.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.util.UserContext;
import com.zapi.workbench.entity.WbApplication;
import com.zapi.workbench.service.WbApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统应用Controller
 * 用于管理对外开放的应用接口
 */
@Api(tags = "工作台-应用管理")
@RestController
@RequestMapping("/api/workbench/application")
public class WbApplicationController {

    @Autowired
    private WbApplicationService wbApplicationService;

    /**
     * 分页查询应用列表
     */
    @ApiOperation("分页查询应用列表")
    @GetMapping("/list")
    public Result<Page<WbApplication>> getApplicationList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("应用名称") @RequestParam(required = false) String appName,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        
        Page<WbApplication> page = new Page<>(current, size);
        QueryWrapper<WbApplication> queryWrapper = new QueryWrapper<>();
        
        if (appName != null && !appName.isEmpty()) {
            queryWrapper.like("app_name", appName);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        Page<WbApplication> result = wbApplicationService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 获取所有启用的应用
     */
    @ApiOperation("获取所有启用的应用")
    @GetMapping("/all")
    public Result<List<WbApplication>> getAllApplications() {
        QueryWrapper<WbApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).orderByDesc("create_time");
        List<WbApplication> applications = wbApplicationService.list(queryWrapper);
        return Result.success(applications);
    }

    /**
     * 根据ID获取应用
     */
    @ApiOperation("根据ID获取应用")
    @GetMapping("/{id}")
    public Result<WbApplication> getApplicationById(@ApiParam("应用ID") @PathVariable Long id) {
        WbApplication application = wbApplicationService.getById(id);
        if (application != null) {
            return Result.success(application);
        }
        return Result.error("应用不存在");
    }

    /**
     * 添加应用（自动生成授权码和密码）
     */
    @ApiOperation("添加应用")
    @PostMapping("/add")
    public Result<WbApplication> addApplication(@RequestBody WbApplication application) {
        // 检查应用名称是否已存在
        QueryWrapper<WbApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", application.getAppName());
        WbApplication existing = wbApplicationService.getOne(queryWrapper);
        if (existing != null) {
            return Result.error("应用名称已存在");
        }
        
        // 创建应用（自动生成授权码和密码）
        WbApplication created = wbApplicationService.createApplication(application);
        return Result.success("添加成功", created);
    }

    /**
     * 更新应用（不能修改授权码和密码）
     */
    @ApiOperation("更新应用")
    @PutMapping("/update")
    public Result<String> updateApplication(@RequestBody WbApplication application) {
        if (application.getId() == null) {
            return Result.error("应用ID不能为空");
        }
        
        // 检查应用是否存在
        WbApplication existing = wbApplicationService.getById(application.getId());
        if (existing == null) {
            return Result.error("应用不存在");
        }
        
        // 检查应用名称是否重复（排除自己）
        QueryWrapper<WbApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", application.getAppName()).ne("id", application.getId());
        WbApplication duplicate = wbApplicationService.getOne(queryWrapper);
        if (duplicate != null) {
            return Result.error("应用名称已存在");
        }
        
        // 保留原有的授权码和密码（不允许通过接口修改）
        application.setAuthCode(existing.getAuthCode());
        application.setAuthPsw(existing.getAuthPsw());
        application.setUpdateTime(LocalDateTime.now());
        
        // 获取当前用户
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId != null) {
                application.setUpdater(String.valueOf(userId));
            }
        } catch (Exception e) {
            // 忽略获取用户失败的情况
        }
        
        boolean success = wbApplicationService.updateById(application);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除应用
     */
    @ApiOperation("删除应用")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteApplication(@ApiParam("应用ID") @PathVariable Long id) {
        boolean success = wbApplicationService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 重新生成授权码和密码
     */
    @ApiOperation("重新生成授权码和密码")
    @PostMapping("/regenerate/{id}")
    public Result<WbApplication> regenerateAuth(@ApiParam("应用ID") @PathVariable Long id) {
        try {
            WbApplication application = wbApplicationService.regenerateAuth(id);
            return Result.success("重新生成成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 切换应用状态
     */
    @ApiOperation("切换应用状态")
    @PutMapping("/status/{id}")
    public Result<String> changeStatus(
            @ApiParam("应用ID") @PathVariable Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        
        WbApplication application = wbApplicationService.getById(id);
        if (application == null) {
            return Result.error("应用不存在");
        }
        
        application.setStatus(status);
        application.setUpdateTime(LocalDateTime.now());
        
        boolean success = wbApplicationService.updateById(application);
        if (success) {
            return Result.success(status == 1 ? "启用成功" : "禁用成功");
        }
        return Result.error("操作失败");
    }
}