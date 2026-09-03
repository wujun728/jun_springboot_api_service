package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.User;
import com.zapi.entity.UserOrg;
import com.zapi.mapper.UserOrgMapper;
import com.zapi.service.UserOrgService;
import com.zapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户组织关联 Service 实现类
 */
@Service
public class UserOrgServiceImpl extends ServiceImpl<UserOrgMapper, UserOrg> implements UserOrgService {

    @Autowired
    private UserService userService;

    @Override
    public List<Long> getOrgIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserOrg::getUserId, userId);
        return list(wrapper).stream()
                .map(UserOrg::getOrgId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserIdsByOrgId(Long orgId) {
        LambdaQueryWrapper<UserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserOrg::getOrgId, orgId);
        return list(wrapper).stream()
                .map(UserOrg::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignOrgsToUser(Long userId, List<Long> orgIds) {
        // 先删除用户现有的所有组织
        removeUserOrgs(userId);

        // 如果组织列表为空，则只删除不添加
        if (orgIds == null || orgIds.isEmpty()) {
            return true;
        }

        // 批量插入新的组织关联
        List<UserOrg> userOrgs = orgIds.stream()
                .map(orgId -> {
                    UserOrg userOrg = new UserOrg();
                    userOrg.setUserId(userId);
                    userOrg.setOrgId(orgId);
                    return userOrg;
                })
                .collect(Collectors.toList());

        return saveBatch(userOrgs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUsersToOrg(Long orgId, List<Long> userIds) {
        // 先删除组织现有的所有用户
        removeOrgUsers(orgId);

        // 如果用户列表为空，则只删除不添加
        if (userIds == null || userIds.isEmpty()) {
            return true;
        }

        // 批量插入新的用户关联
        List<UserOrg> userOrgs = userIds.stream()
                .map(userId -> {
                    UserOrg userOrg = new UserOrg();
                    userOrg.setUserId(userId);
                    userOrg.setOrgId(orgId);
                    return userOrg;
                })
                .collect(Collectors.toList());

        return saveBatch(userOrgs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUserOrgs(Long userId) {
        LambdaQueryWrapper<UserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserOrg::getUserId, userId);
        return remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeOrgUsers(Long orgId) {
        LambdaQueryWrapper<UserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserOrg::getOrgId, orgId);
        return remove(wrapper);
    }

    @Override
    public Page<User> getUsersByOrgId(Long orgId, Integer current, Integer size) {
        // 获取该组织下的所有用户ID
        List<Long> userIds = getUserIdsByOrgId(orgId);
        
        Page<User> page = new Page<>(current, size);
        
        if (userIds == null || userIds.isEmpty()) {
            return page;
        }
        
        // 查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getId, userIds);
        wrapper.eq(User::getDeleted, 0);
        wrapper.orderByDesc(User::getCreateTime);
        
        page = userService.page(page, wrapper);
        
        // 清除密码字段
        page.getRecords().forEach(user -> user.setPassword(null));
        
        return page;
    }
}
