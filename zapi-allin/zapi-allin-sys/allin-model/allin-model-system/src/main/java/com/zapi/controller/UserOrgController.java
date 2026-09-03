package com.zapi.controller;

import com.zapi.common.Result;
import com.zapi.entity.UserOrg;
import com.zapi.service.UserOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户组织关联 Controller
 */
@Api(tags = "用户组织关联管理")
@RestController
@RequestMapping("/system/user-org")
@RequiredArgsConstructor
public class UserOrgController {

    private final UserOrgService userOrgService;

    @ApiOperation("根据用户ID获取组织ID列表")
    @GetMapping("/orgs/{userId}")
    public Result<List<Long>> getOrgIdsByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<Long> orgIds = userOrgService.getOrgIdsByUserId(userId);
        return Result.success(orgIds);
    }

    @ApiOperation("根据组织ID获取用户ID列表")
    @GetMapping("/users/{orgId}")
    public Result<List<Long>> getUserIdsByOrgId(
            @ApiParam("组织ID") @PathVariable Long orgId) {
        List<Long> userIds = userOrgService.getUserIdsByOrgId(orgId);
        return Result.success(userIds);
    }

    @ApiOperation("为用户分配组织")
    @PostMapping("/assign-orgs/{userId}")
    public Result<Boolean> assignOrgsToUser(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("组织ID列表") @RequestBody List<Long> orgIds) {
        boolean success = userOrgService.assignOrgsToUser(userId, orgIds);
        return success ? Result.success("分配组织成功", true) : Result.error("分配组织失败");
    }

    @ApiOperation("为组织分配用户")
    @PostMapping("/assign-users/{orgId}")
    public Result<Boolean> assignUsersToOrg(
            @ApiParam("组织ID") @PathVariable Long orgId,
            @ApiParam("用户ID列表") @RequestBody List<Long> userIds) {
        boolean success = userOrgService.assignUsersToOrg(orgId, userIds);
        return success ? Result.success("分配用户成功", true) : Result.error("分配用户失败");
    }

    @ApiOperation("删除用户的所有组织")
    @DeleteMapping("/user/{userId}")
    public Result<Boolean> removeUserOrgs(
            @ApiParam("用户ID") @PathVariable Long userId) {
        boolean success = userOrgService.removeUserOrgs(userId);
        return success ? Result.success("删除成功", true) : Result.error("删除失败");
    }

    @ApiOperation("删除组织的所有用户关联")
    @DeleteMapping("/org/{orgId}")
    public Result<Boolean> removeOrgUsers(
            @ApiParam("组织ID") @PathVariable Long orgId) {
        boolean success = userOrgService.removeOrgUsers(orgId);
        return success ? Result.success("删除成功", true) : Result.error("删除失败");
    }

    @ApiOperation("获取所有用户组织关联")
    @GetMapping("/list")
    public Result<List<UserOrg>> list() {
        List<UserOrg> list = userOrgService.list();
        return Result.success(list);
    }

    @ApiOperation("根据组织ID获取用户列表（带分页）")
    @GetMapping("/users-by-org/{orgId}")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zapi.entity.User>> getUsersByOrgId(
            @ApiParam("组织ID") @PathVariable Long orgId,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zapi.entity.User> page = 
            userOrgService.getUsersByOrgId(orgId, current, size);
        return Result.success(page);
    }
}
