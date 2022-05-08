package com.itboy.security;

import com.itboy.model.SysPermission;
import com.itboy.model.SysRole;
import com.itboy.model.SysUser;
import com.itboy.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName MyShiroRealm
 * @Description 权限数据源及认证
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/26 0026 16:36
 **/
@Log4j2
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;


    //权限信息，包括角色以及权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser user  = (SysUser)principals.getPrimaryPrincipal();
        for(SysRole role : user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            List<SysPermission> menuList = role.getPermissions();
            for (SysPermission menu:menuList
                 ) {
                authorizationInfo.addStringPermission(menu.getPermission());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String userName = (String)token.getPrincipal();
        SysUser user = loginService.findByUserName(userName);
        if(user == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getUserName()),
                getName()
        );
        return authenticationInfo;
    }


    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }



    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }


    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        log.info("Shiro clearAllCache...");
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
        clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
