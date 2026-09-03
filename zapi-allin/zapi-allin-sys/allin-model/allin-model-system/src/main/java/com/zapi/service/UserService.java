package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.User;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 更新用户信息
     */
    boolean updateUserInfo(User user);

    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
} 