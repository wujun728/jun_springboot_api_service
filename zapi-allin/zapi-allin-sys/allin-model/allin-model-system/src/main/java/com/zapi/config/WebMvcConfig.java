package com.zapi.config;

import com.zapi.interceptor.StatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 
 * @author ZAPI Team
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StatisticsInterceptor statisticsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册统计拦截器
        registry.addInterceptor(statisticsInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .order(1); // 设置拦截器优先级
    }
}

