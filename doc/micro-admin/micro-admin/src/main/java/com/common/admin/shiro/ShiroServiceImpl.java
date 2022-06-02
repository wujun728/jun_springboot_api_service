package com.common.admin.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShiroServiceImpl implements ShiroService{
	@Resource
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 动态加载数据库权限 */
	@Override
	public Map<String, String> loadFilterChainDefinitions() {
		// 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = initDBChainInfo();
        // 配置不会被拦截的链接 顺序判断
        logger.info("………………开始加载静态权限数据………………");
        filterChainDefinitionMap.put("/nh-micro-js/**", "anon");
        filterChainDefinitionMap.put("/micropage/nh-micro-jsp/nhlogin.jsp", "anon");
        filterChainDefinitionMap.put("/micropage/nh-micro-jsp/layout/center.jsp", "anon");
        filterChainDefinitionMap.put("/micromvc/uc/logingo", "anon");
        filterChainDefinitionMap.put("/micromvc/uc/logout", "anon");
        // 验证码
        filterChainDefinitionMap.put("/servlet/imgCheckCode", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/micromvc/uc/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
       
        logger.info("………………权限数据加载完毕：{}", filterChainDefinitionMap);
        return filterChainDefinitionMap;
	}
	
	public Map<String,String> initDBChainInfo(){
		logger.info("………………开始加载数据库权限数据………………");
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		try {
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from nh_micro_sysmenu");
			for(Object obj : list){
				Map menu = (Map)obj;
				if(menu.get("url") == null || "".equals(menu.get("url"))){
					continue;
				}
				filterChainDefinitionMap.put("/"+menu.get("url").toString(),"perms["+menu.get("code").toString()+"]");
			}
		} catch (Exception e) {
			logger.error("…………加载数据库权限数据错误！…………，{}", e.getMessage());
			e.printStackTrace();
		}
		return filterChainDefinitionMap;
	}
	
	/** 更新权限数据 */
	@Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean){
		synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
 
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
 
            // 清空老的权限控制
            manager.getFilterChains().clear();
 
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }

	}
}
