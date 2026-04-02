package com.gitee.freakchicken.dbapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtUtils {

    private static String secret;

    /**
     * 由 JwtConfig 在应用启动时调用，注入配置的 JWT 密钥
     */
    public static void setSecret(String jwtSecret) {
        secret = jwtSecret;
    }

    public static String createToken(String userId) {

        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("JWT secret not initialized");
        }

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 30);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(userId)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .sign(Algorithm.HMAC256(secret));   //加密
    }

    /**
     * 检验合法性，使用应用配置的独立密钥
     *
     * @param token
     */
    public static boolean verifyToken(String token) {

        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("JWT secret not initialized");
        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {

        }
        return audience;
    }

    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}
