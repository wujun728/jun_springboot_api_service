package org.harmony.toddler.mybatis.spring.demo.service.impl;

import org.harmony.toddler.mybatis.spring.demo.mapper.UserMapper;
import org.harmony.toddler.mybatis.spring.demo.service.UserService;
import org.harmony.toddler.mybatis.spring.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserVO> listByCondition(UserVO userVO) {
        return userMapper.selectByCondition(userVO);
    }

    @Override
    public UserVO getByName(String name) {
        return userMapper.selectByName(name);
    }
}