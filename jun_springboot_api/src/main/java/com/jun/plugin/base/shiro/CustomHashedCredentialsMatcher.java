package com.jun.plugin.base.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import com.jun.plugin.base.exception.BusinessException;
import com.jun.plugin.base.service.RedisService;

/**
 * @ClassName: CustomHashedCredentialsMatcher
 * TODO:类文件简单描述
 * @author Wujun
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
