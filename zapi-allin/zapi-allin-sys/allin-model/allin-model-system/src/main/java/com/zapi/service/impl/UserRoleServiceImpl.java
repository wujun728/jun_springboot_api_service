package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.UserRole;
import com.zapi.mapper.UserRoleMapper;
import com.zapi.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联 Service 实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        return list(wrapper).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        return list(wrapper).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除用户现有的所有角色
        removeUserRoles(userId);

        // 如果角色列表为空，则只删除不添加
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }

        // 批量插入新的角色关联
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .collect(Collectors.toList());

        return saveBatch(userRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUserRoles(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        return remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoleUsers(Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        return remove(wrapper);
    }
}
