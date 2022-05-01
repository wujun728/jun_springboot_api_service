package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.UserMapper;
import com.gitee.freakchicken.dbapi.basic.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUser(String username, String password) {
        User user = userMapper.login(username, password);
        return user;
    }

    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Transactional
    public void resetPassword(String password) {
        userMapper.updatePassword(password);
    }
}
