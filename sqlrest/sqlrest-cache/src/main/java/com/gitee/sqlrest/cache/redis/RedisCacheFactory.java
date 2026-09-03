package com.gitee.sqlrest.cache.redis;

import com.gitee.sqlrest.cache.CacheFactory;
import java.util.Map;
import javax.annotation.Resource;

public class RedisCacheFactory implements CacheFactory {

  @Resource
  private JedisClient jedisClient;

  @Override
  public <T> Map<String, T> getCacheMap(String key) {
    return new RedisCacheMap<>(key, jedisClient);
  }
}
