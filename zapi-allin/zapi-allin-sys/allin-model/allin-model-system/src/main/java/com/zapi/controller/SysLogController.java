package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.SysLog;
import com.zapi.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统日志Controller
 */
@Api(tags = "系统日志管理")
@RestController
@RequestMapping("/system/syslog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页查询系统日志
     */
    @ApiOperation("分页查询系统日志")
    @GetMapping("/list")
    public Result<Page<SysLog>> getSysLogList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("用户名") @RequestParam(required = false) String username,
            @ApiParam("操作描述") @RequestParam(required = false) String operation,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime) {
        Page<SysLog> page = new Page<>(current, size);
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (operation != null && !operation.isEmpty()) {
            queryWrapper.like("operation", operation);
        }
        if (startTime != null && !startTime.isEmpty()) {
            queryWrapper.ge("create_time", startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            queryWrapper.le("create_time", endTime);
        }
        
        queryWrapper.orderByDesc("create_time");
        Page<SysLog> result = sysLogService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 根据ID获取系统日志
     */
    @ApiOperation("根据ID获取系统日志")
    @GetMapping("/{id}")
    public Result<SysLog> getSysLogById(@ApiParam("日志ID") @PathVariable Long id) {
        SysLog sysLog = sysLogService.getById(id);
        if (sysLog != null) {
            return Result.success(sysLog);
        }
        return Result.error("日志不存在");
    }

    /**
     * 清空系统日志
     */
    @ApiOperation("清空系统日志")
    @DeleteMapping("/clear")
    public Result<String> clearSysLog() {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        boolean success = sysLogService.remove(queryWrapper);
        if (success) {
            return Result.success("清空成功");
        }
        return Result.error("清空失败");
    }

    /**
     * 删除指定日志
     */
    @ApiOperation("删除指定日志")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteSysLog(@ApiParam("日志ID") @PathVariable Long id) {
        boolean success = sysLogService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
} 