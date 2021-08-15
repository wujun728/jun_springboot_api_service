package com.jun.plugin.api.plugin.token;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jun.plugin.api.plugin.redis.JedisClient;
/**
 * Redis Token存储实现
 * @author Gaojun.Zhou
 * @date 2016年12月29日 下午2:31:08
 */
@Component
public class RedisTokenManager  implements TokenManager {

	@Autowired private JedisClient jedisClient;

	/**
	 * 存储15天
	 */
	public static int timeOut = 60 * 60 * 24 * 15;
	
	/**
	 * 创建Token
	 */
    @Override
    public String createToken(String uid) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        jedisClient.set(token,uid,timeOut);
        return token;
    }
    /**
     * 验证Token
     */
    @Override
    public boolean checkToken(String token) {
    	
        return !StringUtils.isEmpty(jedisClient.get(token));
    }
}
