package com.gitee.sqlrest.cache.redis;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sqlrest.cache.redis")
public class RedisProperties {

  @Data
  public static class Pool {

    private int maxIdle = 8;
    private int minIdle = 0;
    private int maxActive = 8;
    private Duration maxWait = Duration.ofMillis(-1L);
    private Duration timeBetweenEvictionRuns;
  }

  private boolean enabled = false;
  private int database = 0;
  private String host = "127.0.0.1";
  private String username;
  private String password;
  private int port = 6379;
  private boolean ssl = false;
  private Duration timeout = Duration.ofSeconds(1);
  private Duration connectTimeout = Duration.ofSeconds(1);
  private String clientName = "sqlrest";
  private Pool pool;

}
