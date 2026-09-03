package com.gitee.sqlrest.core.exec.module;

import com.gitee.sqlrest.core.exec.annotation.Comment;
import com.gitee.sqlrest.core.exec.annotation.Module;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Module("env")
public class EnvVarModule {

  private final Environment environment;

  public EnvVarModule(Environment environment) {
    this.environment = environment;
  }

  @Comment("获取配置")
  public String get(@Comment("key") String key) {
    return environment.getProperty(key);
  }

  @Comment("获取配置")
  public String get(@Comment("key") String key,
      @Comment("defaultValue") String defaultValue) {
    return environment.getProperty(key, defaultValue);
  }

}
