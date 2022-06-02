package com.common.admin.shiro;

import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

public interface ShiroService {
	
	public Map<String, String> loadFilterChainDefinitions();
	public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean);
}
