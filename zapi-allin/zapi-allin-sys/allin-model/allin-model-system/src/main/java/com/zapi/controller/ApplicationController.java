package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.Application;
import com.zapi.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 应用管理控制器
 */
@RestController
@RequestMapping("/system/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 分页查询应用列表
     */
    @GetMapping("/page")
    public Result<Page<Application>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String appKey,
            @RequestParam(required = false) Integer status) {
        
        Page<Application> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.like(name != null, "name", name)
                .like(appKey != null, "app_key", appKey)
                .eq(status != null, "status", status)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        
        Page<Application> result = applicationService.page(page, queryWrapper);
        return Result.success(result);
    }

    /**
     * 根据ID获取应用信息
     */
    @GetMapping("/{id}")
    public Result<Application> getById(@PathVariable Long id) {
        Application application = applicationService.getById(id);
        if (application != null && application.getDeleted() == 0) {
            return Result.success(application);
        }
        return Result.error("应用不存在");
    }

    /**
     * 新增应用
     */
    @PostMapping
    public Result<String> add(@RequestBody Application application) {
        application.setCreateTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());
        application.setDeleted(0);
        
        boolean result = applicationService.save(application);
        if (result) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新应用
     */
    @PutMapping
    public Result<String> update(@RequestBody Application application) {
        Application existing = applicationService.getById(application.getId());
        if (existing == null || existing.getDeleted() == 1) {
            return Result.error("应用不存在");
        }
        
        existing.setName(application.getName());
        existing.setAppKey(application.getAppKey());
        existing.setAppSecret(application.getAppSecret());
        existing.setDescription(application.getDescription());
        existing.setStatus(application.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        
        boolean result = applicationService.updateById(existing);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除应用
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        Application application = applicationService.getById(id);
        if (application == null || application.getDeleted() == 1) {
            return Result.error("应用不存在");
        }
        
        application.setDeleted(1);
        application.setUpdateTime(LocalDateTime.now());
        
        boolean result = applicationService.updateById(application);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}