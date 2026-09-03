package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名更新密码
     */
    @Update("UPDATE sys_user SET password = #{password} WHERE username = #{username}")
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
    
    /**
     * 根据组织ID查询用户ID列表
     */
    @Select("SELECT user_id FROM sys_user_org WHERE org_id = #{orgId}")
    List<Long> selectUserIdsByOrgId(@Param("orgId") Long orgId);
} 