package com.gitee.freakchicken.dbapi.service;

import com.gitee.freakchicken.dbapi.dao.UserMapper;
import com.gitee.freakchicken.dbapi.domain.User;
import com.gitee.freakchicken.dbapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUser(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            return null;
        }
        // 如果旧密码是明文，自动迁移为 BCrypt
        if (!PasswordUtil.isBCryptHash(user.getPassword())) {
            String bcryptHash = PasswordUtil.hashPassword(password);
            userMapper.updatePasswordById(user.getId(), bcryptHash);
        }
        return user;
    }

    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Transactional
    public void resetPassword(String password) {
        String bcryptHash = PasswordUtil.hashPassword(password);
        userMapper.updatePassword(bcryptHash);
    }
}
