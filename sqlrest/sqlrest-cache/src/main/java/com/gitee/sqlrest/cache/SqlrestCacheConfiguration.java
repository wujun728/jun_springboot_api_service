package com.gitee.sqlrest.cache;

import com.gitee.sqlrest.cache.hazelcast.HazelcastCacheFactory;
import com.gitee.sqlrest.cache.redis.JedisClient;
import com.gitee.sqlrest.cache.redis.RedisCacheFactory;
import com.gitee.sqlrest.cache.redis.RedisProperties;
import com.gitee.sqlrest.common.consts.Constants;
import com.hazelcast.config.Config;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.netflix.discovery.EurekaClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class SqlrestCacheConfiguration {

  @Configuration
  @ConditionalOnProperty(value = "sqlrest.cache.hazelcast.enabled", havingValue = "true")
  static class HazelcastCacheConfig {

    @Bean
    public Config hazelcastCacheConfigFromEureka(EurekaClient eurekaClient) {
      EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
      Config config = new Config();
      config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
      config.getNetworkConfig().getJoin().getEurekaConfig()
          .setEnabled(true)
          .setProperty("self-registration", "true")
          .setProperty("namespace", "hazelcast")
          .setProperty("use-metadata-for-host-and-port", "true")
          .setProperty("skip-eureka-registration-verification", "true");
      config.getMapConfig(Constants.CACHE_KEY_TOKEN_CLIENT)
          .setTimeToLiveSeconds(Constants.CLIENT_TOKEN_DURATION_SECONDS.intValue());
      return config;
    }

    @Bean
    public CacheFactory hazelcastCacheFactory() {
      return new HazelcastCacheFactory();
    }
  }

  @Configuration
  @EnableConfigurationProperties(RedisProperties.class)
  @ConditionalOnProperty(value = "sqlrest.cache.redis.enabled", havingValue = "true")
  static class RedisCacheConfig {

    @Bean
    public JedisPool jedisPool(RedisProperties properties) {
      RedisProperties.Pool pool = properties.getPool();
      JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(pool.getMaxActive());
      poolConfig.setMaxIdle(pool.getMaxIdle());
      poolConfig.setMinIdle(pool.getMinIdle());
      if (pool.getTimeBetweenEvictionRuns() != null) {
        poolConfig.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRuns().toMillis());
      }
      if (pool.getMaxWait() != null) {
        poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
      }
      return new JedisPool(poolConfig, properties.getHost(),
          properties.getPort(), (int) properties.getTimeout().toMillis(),
          properties.getUsername(), properties.getPassword(),
          properties.getDatabase(), properties.isSsl());
    }

    @Bean
    public JedisClient jedisClient(JedisPool jedisPool) {
      return new JedisClient(jedisPool);
    }

    @Bean
    public CacheFactory redisCacheFactory() {
      return new RedisCacheFactory();
    }
  }

}
