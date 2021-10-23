package com.jun.plugin.apiservice.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import com.jun.plugin.apiservice.model.PermissionDto;
import com.jun.plugin.apiservice.model.RoleDto;

/**
 * PermissionMapper
 * @author dolyw.com
 * @date 2018/8/31 14:42
 */
public interface PermissionMapper extends Mapper<PermissionDto> {
    /**
     * 根据Role查询Permission
     * @param roleDto
     * @return java.util.List<com.wang.model.PermissionDto>
     * @author dolyw.com
     * @date 2018/8/31 11:30
     */
    List<PermissionDto> findPermissionByRole(RoleDto roleDto);
}