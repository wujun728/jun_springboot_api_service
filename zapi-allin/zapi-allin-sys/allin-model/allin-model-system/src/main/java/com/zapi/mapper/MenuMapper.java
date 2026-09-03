package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询用户菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询角色菜单列表
     */
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有菜单（树形结构）
     */
    List<Menu> selectMenuTree();
} 