package com.jun.plugin.system.common.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jun.plugin.system.common.exception.BusinessException;
import com.jun.plugin.system.common.exception.code.BaseResponseCode;
import com.jun.plugin.system.common.shiro.jwt.JwtToken;
import com.jun.plugin.system.common.util.JedisUtil;
import com.jun.plugin.system.common.util.JwtUtil;
import com.jun.plugin.system.common.util.common.StringUtil;
import com.jun.plugin.system.common.utils.Constant;
import com.jun.plugin.system.service.PermissionService;
import com.jun.plugin.system.service.RedisService;
import com.jun.plugin.system.service.RoleService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 授权
 *
 * @author wujun
 * @version V1.0
 * @date 2020年3月18日
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {
    @Lazy
    @Autowired
    private PermissionService permissionService;
    @Lazy
    @Autowired
    private RoleService roleService;
    @Lazy
    @Autowired
    private RedisService redisService;
    @Value("${spring.redis.key.prefix.permissionRefresh}")
    private String redisPermissionRefreshKey;
    @Value("${spring.redis.key.prefix.userToken}")
    private String userTokenPrefix;
    @Lazy
    @Autowired
    private RedisService redisDb;


    /**
     * 执行授权逻辑,只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String account = JwtUtil.getClaim(principalCollection.toString(), Constant.ACCOUNT);
        
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        String sessionInfoStr = redisDb.get(userTokenPrefix + principalCollection.getPrimaryPrincipal());
        if (StringUtils.isEmpty(sessionInfoStr)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        JSONObject redisSession = JSON.parseObject(sessionInfoStr);
        if (redisSession == null) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }

        if (redisSession.get(Constant.ROLES_KEY) != null) {
            authorizationInfo.addRoles((Collection<String>) redisSession.get(Constant.ROLES_KEY));
        }
        if (redisSession.get(Constant.PERMISSIONS_KEY) != null) {
            authorizationInfo.addStringPermissions((Collection<String>) redisSession.get(Constant.PERMISSIONS_KEY));
        }
        return authorizationInfo;
    }


    /**
     * 执行认证逻辑,默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getPrincipal(), getName());
    }
    
    
    //******************************************************************************************************
    //******************************************************************************************************
    /**
     * 执行认证逻辑,默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    protected AuthenticationInfo doGetAuthenticationInfoJWT(AuthenticationToken authenticationToken) throws AuthenticationException {
    	String token = (String) authenticationToken.getCredentials();
    	// 解密获得account，用于和数据库进行对比
    	String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
    	// 帐号为空
    	if (StringUtil.isBlank(account)) {
    		throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
    	}
    	// 查询用户是否存在
//        UserDto userDto = new UserDto();
//        userDto.setAccount(account);
//        userDto = userMapper.selectOne(userDto);
//        if (userDto == null) {
//            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
//        }
    	// 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
    	if (JwtUtil.verify(token) && JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
    		// 获取RefreshToken的时间戳
    		String currentTimeMillisRedis = JedisUtil.getObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
    		// 获取AccessToken时间戳，与RefreshToken的时间戳对比
    		if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
    			return new SimpleAuthenticationInfo(token, token, "userRealm");
    		}
    	}
    	throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
    
    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }
}
