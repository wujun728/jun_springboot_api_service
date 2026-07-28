package com.ruoyi.api.plugin.impl;

import com.ruoyi.api.plugin.CachePlugin;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MemoryCachePlugin extends CachePlugin {

    private static final Map<String, Cache<String, Object>> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.logger.info("MemoryCachePlugin initialized");
    }

    @Override
    public void set(String apiId, Map<String, Object> requestParams, Object data, String localPluginParam) {
        Cache<String, Object> cache = getOrCreateCache(apiId, localPluginParam);
        String cacheKey = buildCacheKey(requestParams);
        cache.put(cacheKey, data);
    }

    @Override
    public Object get(String apiId, Map<String, Object> requestParams, String localPluginParam) {
        Cache<String, Object> cache = getOrCreateCache(apiId, localPluginParam);
        String cacheKey = buildCacheKey(requestParams);
        return cache.getIfPresent(cacheKey);
    }

    @Override
    public void clean(String apiId, String localPluginParam) {
        Cache<String, Object> cache = cacheMap.remove(apiId);
        if (cache != null) {
            cache.invalidateAll();
        }
    }

    private Cache<String, Object> getOrCreateCache(String apiId, String localPluginParam) {
        return cacheMap.computeIfAbsent(apiId, k -> {
            long maxSize = 1000;
            long expireSeconds = 300;
            if (localPluginParam != null && !localPluginParam.trim().isEmpty()) {
                try {
                    com.alibaba.fastjson2.JSONObject param = com.alibaba.fastjson2.JSONObject.parseObject(localPluginParam);
                    if (param.containsKey("maxSize")) {
                        maxSize = param.getLongValue("maxSize");
                    }
                    if (param.containsKey("expireSeconds")) {
                        expireSeconds = param.getLongValue("expireSeconds");
                    }
                } catch (Exception ignored) {}
            }
            return Caffeine.newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                    .build();
        });
    }

    private String buildCacheKey(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "_empty_";
        }
        return params.toString();
    }

    @Override
    public String getName() {
        return "Memory Cache";
    }

    @Override
    public String getDescription() {
        return "Caffeine-based LRU cache with configurable max size and TTL";
    }

    @Override
    public String getParamDescription() {
        return "{\"maxSize\":1000,\"expireSeconds\":300}";
    }
}