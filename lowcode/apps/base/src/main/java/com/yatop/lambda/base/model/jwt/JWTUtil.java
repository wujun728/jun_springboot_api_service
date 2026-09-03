package com.yatop.lambda.base.model.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    private final static Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private static final long EXPIRE_TIME = 3600;

    /**
     * 校验 token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            logger.info("token is valid");
            return true;
        } catch (Exception e) {
            logger.info("token is invalid{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 token中获取用户名
     *
     * @return token中包含的用户名
     */
    public static String getLogin(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("login").asString();
        } catch (JWTDecodeException e) {
            logger.error("error：{}", e.getMessage());
            return null;
        }
    }

    public static Map<String, Object> getUser(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Object> user = new HashMap<>(3);
            user.put("login", jwt.getClaim("login").asString());
            user.put("userId", jwt.getClaim("userId").asLong());
            user.put("isAdmin", jwt.getClaim("isAdmin").asBoolean());
            return user;
        } catch (JWTDecodeException e) {
            logger.error("error：{}", e.getMessage());
            return null;
        }
    }


    public static String sign(String username, Long userId, boolean isAdmin, String secret) {
        try {
            username = StringUtils.lowerCase(username);
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("login", username)
                    .withClaim("userId", userId)
                    .withClaim("isAdmin", isAdmin)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            logger.error("error：{}", e);
            return null;
        }
    }

}
