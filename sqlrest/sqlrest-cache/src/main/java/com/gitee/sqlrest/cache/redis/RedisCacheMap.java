package com.gitee.sqlrest.cache.redis;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisCacheMap<V> implements Map<String, V> {

  private final String hashTableKey;
  final private JedisClient jedisClient;

  public RedisCacheMap(String hashTableKey, JedisClient jedisClient) {
    this.hashTableKey = hashTableKey;
    this.jedisClient = jedisClient;
  }

  @Override
  public int size() {
    return jedisClient.doAction(
        jedis -> jedis.hgetAll(hashTableKey).size()
    );
  }

  @Override
  public boolean isEmpty() {
    return 0 == size();
  }

  @Override
  public boolean containsKey(Object o) {
    return jedisClient.doAction(
        jedis -> jedis.hexists(hashTableKey, o.toString())
    );
  }

  @Override
  public boolean containsValue(Object o) {
    return false;
  }

  @Override
  public V get(Object o) {
    return jedisClient.doAction(
        jedis -> {
          String value = jedis.hget(hashTableKey, o.toString());
          return JSONUtil.toBean(value, new TypeReference<V>() {
          }, true);
        }
    );
  }

  @Override
  public V put(String k, V v) {
    return jedisClient.doAction(
        jedis -> {
          String value = jedis.hget(hashTableKey, k);
          jedis.hset(hashTableKey, k, JSONUtil.toJsonStr(v));
          return JSONUtil.toBean(value, new TypeReference<V>() {
          }, true);
        }
    );
  }

  @Override
  public V remove(Object o) {
    return jedisClient.doAction(
        jedis -> {
          String value = jedis.hget(hashTableKey, o.toString());
          jedis.hdel(hashTableKey, o.toString());
          return JSONUtil.toBean(value, new TypeReference<V>() {
          }, true);
        }
    );
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> map) {
    Map<String, String> values = new HashMap<>();
    map.forEach((k, v) -> values.put(k, JSONUtil.toJsonStr(v)));
    jedisClient.doConsume(
        jedis -> jedis.hmset(hashTableKey, values)
    );
  }

  @Override
  public void clear() {
    jedisClient.doConsume(
        jedis -> jedis.hmset(hashTableKey, Collections.emptyMap())
    );
  }

  @Override
  public Set<String> keySet() {
    return jedisClient.doAction(
        jedis -> jedis.hkeys(hashTableKey)
    );
  }

  @Override
  public Collection<V> values() {
    return Collections.emptyList();
  }

  @Override
  public Set<Entry<String, V>> entrySet() {
    return Collections.emptySet();
  }
}
