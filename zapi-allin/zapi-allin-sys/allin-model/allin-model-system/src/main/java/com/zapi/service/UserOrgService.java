package com.zapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.User;
import com.zapi.entity.UserOrg;

import java.util.List;

/**
 * 用户组织关联 Service 接口
 */
public interface UserOrgService extends IService<UserOrg> {

    /**
     * 根据用户ID获取组织ID列表
     *
     * @param userId 用户ID
     * @return 组织ID列表
     */
    List<Long> getOrgIdsByUserId(Long userId);

    /**
     * 根据组织ID获取用户ID列表
     *
     * @param orgId 组织ID
     * @return 用户ID列表
     */
    List<Long> getUserIdsByOrgId(Long orgId);

    /**
     * 为用户分配组织
     *
     * @param userId 用户ID
     * @param orgIds 组织ID列表
     * @return 是否成功
     */
    boolean assignOrgsToUser(Long userId, List<Long> orgIds);

    /**
     * 为组织分配用户
     *
     * @param orgId   组织ID
     * @param userIds 用户ID列表
     * @return 是否成功
     */
    boolean assignUsersToOrg(Long orgId, List<Long> userIds);

    /**
     * 删除用户的所有组织
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean removeUserOrgs(Long userId);

    /**
     * 删除组织的所有用户关联
     *
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean removeOrgUsers(Long orgId);

    /**
     * 根据组织ID获取用户列表（带分页）
     *
     * @param orgId   组织ID
     * @param current 当前页
     * @param size    每页大小
     * @return 用户分页列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> getUsersByOrgId(Long orgId, Integer current, Integer size);
}
