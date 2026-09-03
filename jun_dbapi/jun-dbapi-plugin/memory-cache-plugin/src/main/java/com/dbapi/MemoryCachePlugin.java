package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MemoryCachePlugin extends CachePlugin {

    private static final Map<String, Cache<String, Object>> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.logger.info("内存缓存插件初始化完成");
    }

    @Override
    public void set(ApiConfig config, Map<String, Object> requestParams, Object data, String localPluginParam) {
        Cache<String, Object> cache = getOrCreateCache(config.getId(), localPluginParam);
        String cacheKey = buildCacheKey(requestParams);
        cache.put(cacheKey, data);
    }

    @Override
    public Object get(ApiConfig config, Map<String, Object> requestParams, String localPluginParam) {
        Cache<String, Object> cache = getOrCreateCache(config.getId(), localPluginParam);
        String cacheKey = buildCacheKey(requestParams);
        return cache.getIfPresent(cacheKey);
    }

    @Override
    public void clean(ApiConfig config, String localPluginParam) {
        String apiId = config.getId();
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
                JSONObject param = JSON.parseObject(localPluginParam);
                if (param.containsKey("maxSize")) {
                    maxSize = param.getLongValue("maxSize");
                }
                if (param.containsKey("expireSeconds")) {
                    expireSeconds = param.getLongValue("expireSeconds");
                }
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
        return JSON.toJSONString(params);
    }

    @Override
    public String getName() {
        return "内存缓存插件";
    }

    @Override
    public String getDescription() {
        return "基于Caffeine的内存LRU缓存，支持设置最大缓存数量和过期时间";
    }

    @Override
    public String getParamDescription() {
        return "参数格式：{\"maxSize\":1000,\"expireSeconds\":300}。maxSize为最大缓存数量，默认1000；expireSeconds为缓存过期秒数，默认300秒";
    }
}
