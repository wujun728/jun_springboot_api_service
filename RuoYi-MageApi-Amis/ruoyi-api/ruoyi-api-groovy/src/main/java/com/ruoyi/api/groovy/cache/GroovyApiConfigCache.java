package com.ruoyi.api.groovy.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ruoyi.api.groovy.entity.GroovyApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * API信息缓存（统一静态工具类，key=path）
 */
@Slf4j
public class GroovyApiConfigCache {

    private static final ConcurrentHashMap<String, GroovyApiConfig> cache = new ConcurrentHashMap<>();

    /** path -> beanName 映射（用于 Groovy Class 类型脚本查找 bean） */
    private static final ConcurrentHashMap<String, String> beanNameMap = new ConcurrentHashMap<>();

    public static GroovyApiConfig get(String path) {
        return cache.get(path);
    }

    public static void put(GroovyApiConfig apiInfo) {
        if (apiInfo == null || apiInfo.getPath() == null) {
            return;
        }
        cache.put(apiInfo.getPath(), apiInfo);
        if (apiInfo.getBeanName() != null) {
            beanNameMap.put(apiInfo.getPath(), apiInfo.getBeanName());
        }
    }

    public static void remove(GroovyApiConfig apiInfo) {
        if (apiInfo == null || apiInfo.getPath() == null) {
            return;
        }
        cache.remove(apiInfo.getPath());
        beanNameMap.remove(apiInfo.getPath());
    }

    public static void remove(String path) {
        cache.remove(path);
        beanNameMap.remove(path);
    }

    public static Collection<GroovyApiConfig> getAll() {
        return cache.values();
    }

    public static void clear() {
        cache.clear();
        beanNameMap.clear();
    }

    public static void putAll(List<GroovyApiConfig> apiInfos) {
        clear();
        for (GroovyApiConfig apiInfo : apiInfos) {
            put(apiInfo);
        }
    }

    /**
     * 根据 path 获取 beanName
     */
    public static String getByPath(String path) {
        return beanNameMap.get(path);
    }

    /**
     * 根据 beanName 获取 ApiConfig
     */
    public static GroovyApiConfig getByBeanName(String beanName) {
        for (GroovyApiConfig config : cache.values()) {
            if (beanName.equals(config.getBeanName())) {
                return config;
            }
        }
        return null;
    }

    /**
     * 根据 path 获取 ApiConfig（兼容旧方法 getApiConfigByPath）
     */
    public static GroovyApiConfig getApiConfigByPath(String path) {
        GroovyApiConfig info = cache.get(path);
        if (ObjectUtils.isEmpty(info)) {
            log.error("Path-{} 没有注册的ApiConfig", path);
        }
        return info;
    }

    /**
     * 批量更新缓存（兼容旧方法 put2map）
     */
    public static void put2map(List<GroovyApiConfig> groovyList) {
        putAll(groovyList);
    }

    /**
     * 增量更新
     */
    public static void update2map(List<GroovyApiConfig>[] groovyInfos) {
        List<GroovyApiConfig> addedApiConfigs = groovyInfos[0];
        List<GroovyApiConfig> updatedApiConfigs = groovyInfos[1];
        List<GroovyApiConfig> deletedApiConfigs = groovyInfos[2];
        for (GroovyApiConfig c : addedApiConfigs) { put(c); }
        for (GroovyApiConfig c : updatedApiConfigs) { put(c); }
        for (GroovyApiConfig c : deletedApiConfigs) { remove(c); }
    }

    public static Map<String, GroovyApiConfig> getApiConfigs() {
        return cache;
    }

    public static Map<String, String> getBeanNameMap() {
        return beanNameMap;
    }
}