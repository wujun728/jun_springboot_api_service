package org.harmony.toddler.mybatis.spring.demo.service;

import org.harmony.toddler.mybatis.spring.demo.vo.UserVO;

import java.util.List;

/**
 * User Service Interface
 */
public interface UserService {

    List<UserVO> listByCondition(UserVO userVO);

    UserVO getByName(String name);
}