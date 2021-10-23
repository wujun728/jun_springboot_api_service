package com.jun.plugin.apiservice.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import com.jun.plugin.apiservice.model.RoleDto;
import com.jun.plugin.apiservice.model.UserDto;

/**
 * RoleMapper
 * @author dolyw.com
 * @date 2018/8/31 14:42
 */
public interface RoleMapper extends Mapper<RoleDto> {
    /**
     * 根据User查询Role
     * @param userDto
     * @return java.util.List<com.wang.model.RoleDto>
     * @author dolyw.com
     * @date 2018/8/31 11:30
     */
    List<RoleDto> findRoleByUser(UserDto userDto);
}