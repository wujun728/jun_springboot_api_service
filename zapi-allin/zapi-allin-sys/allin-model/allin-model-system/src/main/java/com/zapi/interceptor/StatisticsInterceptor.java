package com.zapi.interceptor;

import com.zapi.service.SystemStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统统计拦截器
 * 拦截所有请求，记录系统点击次数
 * 
 * @author ZAPI Team
 */
@Slf4j
@Component
public class StatisticsInterceptor implements HandlerInterceptor {

    @Autowired
    private SystemStatisticsService systemStatisticsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 获取请求路径
            String uri = request.getRequestURI();
            
            // 排除不需要统计的请求
            if (shouldExclude(uri)) {
                return true;
            }
            
            // 只统计 GET 和 POST 请求（业务请求）
            String method = request.getMethod();
            if ("GET".equals(method) || "POST".equals(method)) {
                // 异步记录点击，不影响主业务
                systemStatisticsService.recordClick();
            }
        } catch (Exception e) {
            // 统计失败不影响主业务
            log.error("记录系统点击失败", e);
        }
        
        return true;
    }

    /**
     * 判断是否需要排除该请求
     * @param uri 请求路径
     * @return 是否排除
     */
    private boolean shouldExclude(String uri) {
        // 排除静态资源
        if (uri.contains(".")) {
            String extension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();
            if (extension.matches("js|css|jpg|jpeg|png|gif|ico|svg|woff|woff2|ttf|eot")) {
                return true;
            }
        }
        
        // 排除 Swagger 相关请求
        if (uri.contains("/swagger") || uri.contains("/v2/api-docs") || 
            uri.contains("/webjars") || uri.contains("/doc.html")) {
            return true;
        }
        
        // 排除健康检查等
        if (uri.contains("/actuator") || uri.contains("/health")) {
            return true;
        }
        
        // 排除统计接口本身（避免死循环）
        if (uri.contains("/system/statistics")) {
            return true;
        }
        
        return false;
    }
}

