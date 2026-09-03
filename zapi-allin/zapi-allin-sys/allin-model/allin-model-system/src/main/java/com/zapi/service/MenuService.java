package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.Menu;

import java.util.List;

/**
 * 菜单Service接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户ID获取用户菜单列表
     */
    List<Menu> getUserMenus(Long userId);

    /**
     * 根据角色ID获取角色菜单列表
     */
    List<Menu> getRoleMenus(Long roleId);

    /**
     * 获取菜单树形结构
     */
    List<Menu> getMenuTree();

    /**
     * 构建菜单树形结构
     */
    List<Menu> buildMenuTree(List<Menu> menus);
} 