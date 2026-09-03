package com.gitee.sqlrest.cache;

import java.util.Map;

public interface CacheFactory {

  <T> Map<String, T> getCacheMap(String key);
}
