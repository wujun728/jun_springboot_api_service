package com.ruoyi.api.core.spi;

public interface CacheProvider {

    void put(String key, Object value, long expireSeconds);

    Object get(String key);

    void invalid(String key);

    boolean exists(String key);

}