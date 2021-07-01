package com.yingxue.lesson.shiro;

import com.yingxue.lesson.service.RedisService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CustomRealm
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private RedisService redisService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String sessionId= (String) principalCollection.getPrimaryPrincipal();
        String userId= (String) redisService.get(sessionId);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addRoles(getRolesByUserId(userId));
        info.addStringPermissions(getPermissionByUserId(userId));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomPasswordToken token= (CustomPasswordToken) authenticationToken;
        String sessionId= (String) token.getPrincipal();
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(sessionId,sessionId,CustomRealm.class.getName());
        return info;
    }

    /**
     * 模拟通过数据库获取权限数据
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param userId
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getPermissionByUserId(String userId) {
        List<String> permissions = new ArrayList<>();
        /**
         * 只有是 admin 用户才拥有所有权限
         */
        if(userId.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")){
            permissions.add("*");
        }else {
            permissions.add("sys:user:edit");
            permissions.add("sys:user:list");
        }
        return permissions;
    }

    /**
     * 模拟通过数据库获取用户角色信息
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param userId
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getRolesByUserId(String userId) {
        List<String> roles = new ArrayList<>();
        if(userId.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")){
            roles.add("admin");
        }else {
            roles.add("test");
        }

        return roles;
    }
}
