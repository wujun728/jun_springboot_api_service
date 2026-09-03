package com.gitee.sqlrest.common.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class CacheUtils {

  // 缓存时间 2 hours
  public static final long CACHE_DURATION_SECONDS = 7200L;

  private static Cache<String, Object> loadingCache = CacheBuilder.newBuilder()
      /*设置缓存容器的初始容量大小为10*/
      .initialCapacity(10)
      /*设置缓存容器的最大容量大小为1000*/
      .maximumSize(1000)
      /*设置记录缓存命中率*/
      .recordStats()
      /*设置并发级别为8，智并发基本值可以同事些缓存的线程数*/
      .concurrencyLevel(8)
      /*设置过期时间为15分钟*/
      .expireAfterAccess(CACHE_DURATION_SECONDS, TimeUnit.SECONDS)
      .build();

  public static void put(String key, Object value) {
    loadingCache.put(key, value);
  }

  public static Object get(String key) {
    return loadingCache.getIfPresent(key);
  }

  public static void remove(String key) {
    loadingCache.invalidate(key);
  }

  public static void clear() {
    loadingCache.invalidateAll();
  }

  public static Map<String, Object> getAll() {
    return loadingCache.asMap();
  }

  public static Collection<Object> getAllValue() {
    return loadingCache.asMap().values();
  }

}