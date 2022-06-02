package org.harmony.toddler.mybatis.spring.demo.mapper;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.harmony.toddler.mybatis.groovy.GroovyLangDriver;
import org.harmony.toddler.mybatis.spring.demo.vo.UserVO;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("UserMapperSql#selectByCondition")
    @Lang(GroovyLangDriver.class)
    List<UserVO> selectByCondition(UserVO userVO);

    @Select("UserMapperSelectByName")
    @Lang(GroovyLangDriver.class)
    UserVO selectByName(String name);
}
