package com.gitee.sqlrest.executor.config;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.executor.interceptor.ExecutorInterceptor;
import com.gitee.sqlrest.persistence.dao.SystemParamDao;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ExecutorWebConfig implements WebMvcConfigurer {

  private final String pathPattern = Constants.API_DOC_PATH_PREFIX + "/**";

  @Resource
  private SystemParamDao systemParamDao;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(pathPattern).addResourceLocations("classpath:/apidoc/");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    ExecutorInterceptor interceptor = new ExecutorInterceptor(systemParamDao);
    registry.addInterceptor(interceptor).addPathPatterns(pathPattern);
  }

}
