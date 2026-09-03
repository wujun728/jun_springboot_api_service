package com.zapi.openapi.config;

import com.zapi.openapi.interceptor.AppAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 应用认证配置
 * 注册应用认证拦截器
 */
@Configuration
public class AppAuthConfig implements WebMvcConfigurer {

    @Autowired
    private AppAuthInterceptor appAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册应用认证拦截器，拦截所有/openapi/**的请求
        registry.addInterceptor(appAuthInterceptor)
                .addPathPatterns("/openapi/**")
                .excludePathPatterns("/openapi/application/**"); // 排除应用管理接口，避免循环认证
    }
}