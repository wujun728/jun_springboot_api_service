package com.common.admin.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager, ShiroServiceImpl shiroServiceImpl) {
		logger.info("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/micropage/nh-micro-jsp/nhlogin.jsp");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/micropage/nh-micro-jsp/index-new.jsp");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/micropage/nh-micro-jsp/403.jsp");
        // 设置拦截器(动态加载)
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroServiceImpl.loadFilterChainDefinitions());
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }


    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }
}
