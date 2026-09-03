package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.Menu;
import com.zapi.service.MenuService;
import com.zapi.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单Controller
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取当前用户菜单列表
     */
    @ApiOperation("获取当前用户菜单列表")
    @GetMapping("/user")
    public Result<List<Menu>> getUserMenus() {
        try {
            // 获取当前登录用户ID
            Long userId = UserContext.getCurrentUserId();
            
            // 从数据库查询用户菜单
            List<Menu> menus = menuService.getUserMenus(userId);
            return Result.success(menus);
        } catch (Exception e) {
            return Result.error("获取菜单失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询菜单列表
     */
    @ApiOperation("分页查询菜单列表")
    @GetMapping("/list")
    public Result<Page<Menu>> getMenuList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("菜单名称") @RequestParam(required = false) String menuName) {
        Page<Menu> page = new Page<>(current, size);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        
        // 过滤已删除的记录
        queryWrapper.eq("deleted", 0);
        
        if (menuName != null && !menuName.isEmpty()) {
            queryWrapper.like("menu_name", menuName);
        }
        
        queryWrapper.orderByAsc("sort").orderByDesc("create_time");
        Page<Menu> result = menuService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 获取菜单树形结构
     */
    @ApiOperation("获取菜单树形结构")
    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree() {
        try {
            List<Menu> menuTree = menuService.getMenuTree();
            return Result.success(menuTree);
        } catch (Exception e) {
            return Result.error("获取菜单树失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取菜单
     */
    @ApiOperation("根据ID获取菜单")
    @GetMapping("/{id}")
    public Result<Menu> getMenuById(@ApiParam("菜单ID") @PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu != null) {
            return Result.success(menu);
        }
        return Result.error("菜单不存在");
    }

    /**
     * 添加菜单
     */
    @ApiOperation("添加菜单")
    @PostMapping("/add")
    public Result<String> addMenu(@RequestBody Menu menu) {
        // 检查菜单编码是否已存在（排除已删除的）
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_code", menu.getMenuCode())
                   .eq("deleted", 0);
        if (menuService.count(queryWrapper) > 0) {
            return Result.error("菜单编码已存在");
        }
        
        boolean success = menuService.save(menu);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新菜单
     */
    @ApiOperation("更新菜单")
    @PutMapping("/update")
    public Result<String> updateMenu(@RequestBody Menu menu) {
        // 检查菜单编码是否已存在（排除自己和已删除的）
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_code", menu.getMenuCode())
                   .ne("id", menu.getId())
                   .eq("deleted", 0);
        if (menuService.count(queryWrapper) > 0) {
            return Result.error("菜单编码已存在");
        }
        
        boolean success = menuService.updateById(menu);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteMenu(@ApiParam("菜单ID") @PathVariable Long id) {
        // 检查是否有未删除的子菜单
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id)
                   .eq("deleted", 0);
        if (menuService.count(queryWrapper) > 0) {
            return Result.error("存在子菜单，无法删除");
        }
        
        boolean success = menuService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
} 