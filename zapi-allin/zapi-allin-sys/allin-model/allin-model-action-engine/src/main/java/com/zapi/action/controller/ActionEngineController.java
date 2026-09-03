package com.zapi.action.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.action.model.entity.ActionEngine;
import com.zapi.action.service.ActionEngineService;
import com.zapi.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 动作执行引擎控制器
 * 
 * @author zapi
 * @date 2025-01-12
 */
@Api(tags = "动作执行引擎")
@RestController
@RequestMapping("/api/action-engine")
public class ActionEngineController {

    @Autowired
    private ActionEngineService actionEngineService;

    @ApiOperation("测试接口")
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("动作执行引擎模块运行正常");
    }

    @ApiOperation("创建动作")
    @PostMapping
    public Result<ActionEngine> createAction(@RequestBody ActionEngine actionEngine) {
        boolean success = actionEngineService.save(actionEngine);
        if (success) {
            return Result.success(actionEngine);
        }
        return Result.error("创建动作失败");
    }

    @ApiOperation("更新动作")
    @PutMapping
    public Result<ActionEngine> updateAction(@RequestBody ActionEngine actionEngine) {
        boolean success = actionEngineService.updateById(actionEngine);
        if (success) {
            return Result.success(actionEngine);
        }
        return Result.error("更新动作失败");
    }

    @ApiOperation("删除动作")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAction(@ApiParam("动作ID") @PathVariable Long id) {
        boolean success = actionEngineService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除动作失败");
    }

    @ApiOperation("获取动作详情")
    @GetMapping("/{id}")
    public Result<ActionEngine> getActionDetail(@ApiParam("动作ID") @PathVariable Long id) {
        ActionEngine actionEngine = actionEngineService.getById(id);
        if (actionEngine != null) {
            return Result.success(actionEngine);
        }
        return Result.error("动作不存在");
    }

    @ApiOperation("分页查询动作列表")
    @GetMapping("/list")
    public Result<Page<ActionEngine>> getActionList(
            @ApiParam("应用ID") @RequestParam(required = false) String appId,
            @ApiParam("动作名称") @RequestParam(required = false) String actionName,
            @ApiParam("动作类型") @RequestParam(required = false) String actionType,
            @ApiParam("状态") @RequestParam(required = false) Integer status,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<ActionEngine> page = new Page<>(current, size);
        LambdaQueryWrapper<ActionEngine> queryWrapper = new LambdaQueryWrapper<>();
        
        // 应用ID过滤（必须）
        if (StringUtils.hasText(appId)) {
            queryWrapper.eq(ActionEngine::getAppId, appId);
        }
        
        // 动作名称模糊查询
        if (StringUtils.hasText(actionName)) {
            queryWrapper.like(ActionEngine::getActionName, actionName);
        }
        
        // 动作类型过滤
        if (StringUtils.hasText(actionType)) {
            queryWrapper.eq(ActionEngine::getActionType, actionType);
        }
        
        // 状态过滤
        if (status != null) {
            queryWrapper.eq(ActionEngine::getStatus, status);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(ActionEngine::getCreateTime);
        
        Page<ActionEngine> result = actionEngineService.page(page, queryWrapper);
        return Result.success(result);
    }

    @ApiOperation("启用/禁用动作")
    @PutMapping("/{id}/status")
    public Result<Void> changeActionStatus(
            @ApiParam("动作ID") @PathVariable Long id,
            @ApiParam("状态：0-禁用，1-启用") @RequestParam Integer status
    ) {
        ActionEngine actionEngine = new ActionEngine();
        actionEngine.setId(id);
        actionEngine.setStatus(status);
        boolean success = actionEngineService.updateById(actionEngine);
        if (success) {
            return Result.success();
        }
        return Result.error("更新状态失败");
    }

    @ApiOperation("执行动作")
    @PostMapping("/{id}/execute")
    public Result<String> executeAction(
            @ApiParam("动作ID") @PathVariable Long id,
            @ApiParam("执行参数") @RequestBody(required = false) Object params
    ) {
        // TODO: 实现动作执行逻辑
        return Result.success("动作执行成功");
    }
}
