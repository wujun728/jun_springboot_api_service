package com.vacomall.service;

import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.SysUser;

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