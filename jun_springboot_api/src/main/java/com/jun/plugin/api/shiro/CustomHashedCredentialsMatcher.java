package com.jun.plugin.api.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import com.jun.plugin.api.exception.BusinessException;
import com.jun.plugin.api.service.RedisService;

/**
 * @ClassName: CustomHashedCredentialsMatcher
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Autowired
    private RedisService redisService;
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        CustomPasswordToken customPasswordToken= (CustomPasswordToken) token;
        String sessionId= (String) customPasswordToken.getPrincipal();
        if(!redisService.hasKey(sessionId)){
            throw new BusinessException(4010001,"用户授权信息无效请重新登录");
        }
        return true;
    }
}