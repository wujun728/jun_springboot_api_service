package com.gitee.sqlrest.cache.hazelcast;

import com.gitee.sqlrest.cache.CacheFactory;
import com.hazelcast.core.HazelcastInstance;
import java.util.Map;
import javax.annotation.Resource;

public class HazelcastCacheFactory implements CacheFactory {

  @Resource
  private HazelcastInstance hazelcastInstance;

  @Override
  public <T> Map<String, T> getCacheMap(String key) {
    return hazelcastInstance.getMap(key);
  }

}
