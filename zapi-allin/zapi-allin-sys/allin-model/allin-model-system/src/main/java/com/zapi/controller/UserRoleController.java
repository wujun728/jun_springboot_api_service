package com.zapi.controller;

import com.zapi.common.Result;
import com.zapi.entity.UserRole;
import com.zapi.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户角色关联 Controller
 */
@Api(tags = "用户角色关联管理")
@RestController
@RequestMapping("/system/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @ApiOperation("根据用户ID获取角色ID列表")
    @GetMapping("/roles/{userId}")
    public Result<List<Long>> getRoleIdsByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        return Result.success(roleIds);
    }

    @ApiOperation("根据角色ID获取用户ID列表")
    @GetMapping("/users/{roleId}")
    public Result<List<Long>> getUserIdsByRoleId(
            @ApiParam("角色ID") @PathVariable Long roleId) {
        List<Long> userIds = userRoleService.getUserIdsByRoleId(roleId);
        return Result.success(userIds);
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("/assign/{userId}")
    public Result<Boolean> assignRolesToUser(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("角色ID列表") @RequestBody List<Long> roleIds) {
        boolean success = userRoleService.assignRolesToUser(userId, roleIds);
        return success ? Result.success("分配角色成功", true) : Result.error("分配角色失败");
    }

    @ApiOperation("删除用户的所有角色")
    @DeleteMapping("/user/{userId}")
    public Result<Boolean> removeUserRoles(
            @ApiParam("用户ID") @PathVariable Long userId) {
        boolean success = userRoleService.removeUserRoles(userId);
        return success ? Result.success("删除成功", true) : Result.error("删除失败");
    }

    @ApiOperation("删除角色的所有用户关联")
    @DeleteMapping("/role/{roleId}")
    public Result<Boolean> removeRoleUsers(
            @ApiParam("角色ID") @PathVariable Long roleId) {
        boolean success = userRoleService.removeRoleUsers(roleId);
        return success ? Result.success("删除成功", true) : Result.error("删除失败");
    }

    @ApiOperation("获取所有用户角色关联")
    @GetMapping("/list")
    public Result<List<UserRole>> list() {
        List<UserRole> list = userRoleService.list();
        return Result.success(list);
    }
}
