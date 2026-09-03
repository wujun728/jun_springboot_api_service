package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.Menu;
import com.zapi.entity.User;
import com.zapi.mapper.MenuMapper;
import com.zapi.mapper.UserMapper;
import com.zapi.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单Service实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Menu> getUserMenus(Long userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        // 根据用户角色查询菜单
        List<Menu> menus = menuMapper.selectMenuTree();
        // 构建树形结构
        return buildMenuTree(menus);
    }

    @Override
    public List<Menu> getRoleMenus(Long roleId) {
        List<Menu> menus = menuMapper.selectMenusByRoleId(roleId);
        return buildMenuTree(menus);
    }

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menus = menuMapper.selectMenuTree();
        return buildMenuTree(menus);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        // 按父ID分组
        Map<Long, List<Menu>> menuMap = menus.stream()
                .collect(Collectors.groupingBy(menu -> menu.getParentId() != null ? menu.getParentId() : 0L));

        // 递归构建树形结构
        return buildTreeRecursive(menuMap, 0L);
    }

    /**
     * 递归构建树形结构
     */
    private List<Menu> buildTreeRecursive(Map<Long, List<Menu>> menuMap, Long parentId) {
        List<Menu> children = menuMap.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }

        for (Menu menu : children) {
            List<Menu> childMenus = buildTreeRecursive(menuMap, menu.getId());
            if (!childMenus.isEmpty()) {
                menu.setChildren(childMenus);
            }
        }

        return children;
    }
} 