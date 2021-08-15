package com.jun.plugin.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.jun.plugin.api.entity.SysUser;

/**
 *
 * SysUser 表数据服务层接口
 *
 */
public interface ISysUserService extends IService<SysUser> {
	
	/**
	 * 登录
	 */
	SysUser login(SysUser sysUser);

}