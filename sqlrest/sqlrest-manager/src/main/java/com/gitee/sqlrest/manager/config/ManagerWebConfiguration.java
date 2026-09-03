package com.gitee.sqlrest.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ManagerWebConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 访问http://localhost:8080/index.html 都会跳转到classpath:/index.html下去找，即resources/index.html
    registry.addResourceHandler("/index.html").addResourceLocations("classpath:/index.html");
    registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
    // 访问http://localhost:8080/static/*** 都会跳转到classpath:/static/下去找，即resources/static/
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("forward:index.html");
  }
}
