package com.gitee.freakchicken.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username} and password = #{password} limit 1")
    User login(@Param("username") String username, @Param("password") String password);

    @Select("select * from user where username = #{username} limit 1")
    User findByUsername(@Param("username") String username);

    @Update("update user set password = #{password} where username = 'admin' ")
    void updatePassword(String password);

    @Update("update user set password = #{password} where id = #{id}")
    void updatePasswordById(@Param("id") Integer id, @Param("password") String password);
}
