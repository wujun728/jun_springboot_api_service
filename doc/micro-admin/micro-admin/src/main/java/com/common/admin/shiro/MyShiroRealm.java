package com.common.admin.shiro;



import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nh.micro.rule.engine.core.GroovyExecUtil;

public class MyShiroRealm extends AuthorizingRealm{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		List roleList = (List) GroovyExecUtil.execGroovyRetObj("nhuser_user", "getAllRolesIdByUid", userName);
		List list = (List) GroovyExecUtil.execGroovyRetObj("nhuser_ref_menu_role", "getUserPermissions", roleList);
		Set<String> roleNames = new HashSet<String>();
		Set<String> permissions = new HashSet<String>();
		for(Object role : roleList){
			roleNames.add(role.toString());// 添加角色
		}
		for(Object per : list){
			permissions.add(per.toString());// 添加权限
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

	/*
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		if (StringUtils.isBlank(token.getUsername())) {
			throw new AccountException("Null usernames are not allowed by this realm.");
		}
		Map map = getUserInfo(token.getUsername());
		if(map == null){
			logger.error("用户已停用或不存在 [{}]", token.getUsername());
			throw new UnknownAccountException("没有找到用户 [" + token.getUsername() + "]");
		}
		String password = map.get("user_password").toString();
		return new SimpleAuthenticationInfo(map.get("user_id").toString(), password, getName());
	}

	private Map getUserInfo(String userName) {
		List list = (List) GroovyExecUtil.execGroovyRetObj("nhuser_user", "getUserByUserName", userName);
		return list!=null&&list.size()>0 ? (Map)list.get(0) : null;
	}
}
