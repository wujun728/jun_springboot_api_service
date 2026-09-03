package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.Role;
import com.zapi.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色Controller
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     */
    @ApiOperation("分页查询角色列表")
    @GetMapping("/list")
    public Result<Page<Role>> getRoleList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("角色名称") @RequestParam(required = false) String roleName) {
        Page<Role> page = new Page<>(current, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        
        // 过滤已删除的记录
        queryWrapper.eq("deleted", 0);
        
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.like("role_name", roleName);
        }
        
        queryWrapper.orderByAsc("sort").orderByDesc("create_time");
        Page<Role> result = roleService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 获取所有角色
     */
    @ApiOperation("获取所有角色")
    @GetMapping("/all")
    public Result<List<Role>> getAllRoles() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0).eq("status", 1).orderByAsc("sort");
        List<Role> roles = roleService.list(queryWrapper);
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色
     */
    @ApiOperation("根据ID获取角色")
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@ApiParam("角色ID") @PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role != null) {
            return Result.success(role);
        }
        return Result.error("角色不存在");
    }

    /**
     * 添加角色
     */
    @ApiOperation("添加角色")
    @PostMapping("/add")
    public Result<String> addRole(@RequestBody Role role) {
        // 检查角色编码是否已存在（排除已删除的）
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", role.getRoleCode()).eq("deleted", 0);
        Role existingRole = roleService.getOne(queryWrapper);
        if (existingRole != null) {
            return Result.error("角色编码已存在");
        }
        
        // 设置默认状态
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        
        boolean success = roleService.save(role);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新角色
     */
    @ApiOperation("更新角色")
    @PutMapping("/update")
    public Result<String> updateRole(@RequestBody Role role) {
        // 检查角色编码是否重复（排除自己和已删除的）
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", role.getRoleCode()).ne("id", role.getId()).eq("deleted", 0);
        Role existingRole = roleService.getOne(queryWrapper);
        if (existingRole != null) {
            return Result.error("角色编码已存在");
        }
        
        boolean success = roleService.updateById(role);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteRole(@ApiParam("角色ID") @PathVariable Long id) {
        boolean success = roleService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
} 