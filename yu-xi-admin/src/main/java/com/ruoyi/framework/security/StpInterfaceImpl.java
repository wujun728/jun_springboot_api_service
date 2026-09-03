package com.ruoyi.framework.security;

import cn.dev33.satoken.stp.StpInterface;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysRole;
import com.ruoyi.project.system.mapper.SysMenuMapper;
import com.ruoyi.project.system.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ：histone
 * Sa-Token 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    SysMenuMapper menuMapper;

    @Autowired
    SysRoleMapper roleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = new ArrayList<>();
        if (SecurityUtils.isAdmin()) {
            permissionList.add("*");
            return permissionList;
        }
        List<String> perms = menuMapper.selectMenuPermsByUserId((Long) loginId);
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permissionList.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = new ArrayList<>();
        if (SecurityUtils.isAdmin()) {
            roleList.add("*");
            return roleList;
        }
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId((Long) loginId);
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                roleList.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return roleList;
    }

}

