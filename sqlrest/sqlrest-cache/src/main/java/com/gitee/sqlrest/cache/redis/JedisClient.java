package com.gitee.sqlrest.cache.redis;

import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClient {

  private JedisPool jedisPool;

  public JedisClient(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  private Jedis getJedis() {
    Jedis jedis = this.jedisPool.getResource();
    jedis.ping();
    return jedis;
  }

  public <T> T doAction(Function<Jedis, T> function) {
    Jedis jedis = getJedis();
    try {
      return function.apply(jedis);
    } finally {
      jedis.close();
    }
  }

  public void doConsume(Consumer<Jedis> action) {
    Jedis jedis = getJedis();
    try {
      action.accept(jedis);
    } finally {
      jedis.close();
    }
  }
}
