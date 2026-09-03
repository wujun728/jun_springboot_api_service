package com.zapi.openapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.openapi.entity.OpenApiLog;
import com.zapi.openapi.service.OpenApiLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 接口调用日志Controller
 */
@Api(tags = "开放接口-调用日志")
@RestController
@RequestMapping("/openapi/log")
public class OpenApiLogController {

    @Autowired
    private OpenApiLogService openApiLogService;

    /**
     * 分页查询日志列表
     */
    @ApiOperation("分页查询日志列表")
    @GetMapping("/list")
    public Result<Page<OpenApiLog>> getLogList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("应用ID") @RequestParam(required = false) Long appId,
            @ApiParam("配置ID") @RequestParam(required = false) Long configId,
            @ApiParam("调用类型") @RequestParam(required = false) Integer callType,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        
        Page<OpenApiLog> result = openApiLogService.getLogPage(
                current, size, appId, configId, callType, status);
        return Result.success(result);
    }

    /**
     * 根据ID获取日志详情
     */
    @ApiOperation("根据ID获取日志详情")
    @GetMapping("/{id}")
    public Result<OpenApiLog> getLogById(@PathVariable Long id) {
        OpenApiLog log = openApiLogService.getById(id);
        if (log != null) {
            return Result.success(log);
        }
        return Result.error("日志不存在");
    }

    /**
     * 清理历史日志
     */
    @ApiOperation("清理历史日志")
    @DeleteMapping("/clean")
    public Result<String> cleanHistoryLogs(
            @ApiParam("保留天数") @RequestParam(defaultValue = "30") Integer days) {
        int count = openApiLogService.cleanHistoryLogs(days);
        return Result.success("清理成功，共删除 " + count + " 条日志");
    }
}
