package com.ruoyi.auth.service;

import com.ruoyi.common.cloud.constants.SecurityConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.api.RemoteUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 * 处理微服务模式下的用户认证逻辑
 *
 * @author ruoyi
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${token.secret:abcdefghijklmnopqrstuvwxyz}")
    private String secret;

    @Value("${token.expireTime:30}")
    private int expireTime;

    private static final String TOKEN_PREFIX = "login_tokens:";

    /**
     * 登录
     */
    public String login(String username, String password, String code, String uuid) {
        // 通过Feign调用系统服务获取用户信息
        AjaxResult userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        if (userResult == null || (int) userResult.get("code") != 200) {
            throw new RuntimeException("用户不存在/密码错误");
        }
        // 创建token
        String userKey = UUID.randomUUID().toString();
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.USER_KEY, userKey);
        claims.put(SecurityConstants.DETAILS_USER_ID, userResult.get("userId"));
        claims.put(SecurityConstants.DETAILS_USERNAME, username);
        String token = createToken(claims);
        // 缓存登录信息
        redisTemplate.opsForValue().set(TOKEN_PREFIX + userKey, userResult.get("data"), expireTime, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 登出
     */
    public void logout(String token) {
        if (token != null && !token.isEmpty()) {
            try {
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                Claims claims = parseToken(token);
                String userKey = (String) claims.get(SecurityConstants.USER_KEY);
                redisTemplate.delete(TOKEN_PREFIX + userKey);
            } catch (Exception e) {
                log.warn("登出处理异常: {}", e.getMessage());
            }
        }
    }

    /**
     * 刷新令牌
     */
    public void refreshToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = parseToken(token);
        String userKey = (String) claims.get(SecurityConstants.USER_KEY);
        redisTemplate.expire(TOKEN_PREFIX + userKey, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 创建JWT令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 解析JWT令牌
     */
    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
