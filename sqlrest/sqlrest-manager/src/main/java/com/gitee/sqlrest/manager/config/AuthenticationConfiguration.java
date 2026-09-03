package com.gitee.sqlrest.manager.config;

import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.CacheUtils;
import com.gitee.sqlrest.common.util.TokenUtils;
import com.gitee.sqlrest.persistence.dao.SystemUserDao;
import com.gitee.sqlrest.persistence.entity.SystemUserEntity;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationConfiguration implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authenticationInterceptor())
        .excludePathPatterns(Arrays.asList(
            "/user/login",
            "/api/**",
            "/js/**",
            "/css/**",
            "/fonts/**",
            "/index.html",
            "/favicon.ico",
            "/swagger-resources/**",
            "/swagger-resources",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/**",
            "/v3/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**",
            "/eureka/**",
            "/error**",
            "/dashboard",
            "/dashboard/**"
        ))
        .addPathPatterns("/**");
  }

  @Bean
  public HandlerInterceptor authenticationInterceptor() {
    return new HandlerInterceptor() {

      @Resource
      private SystemUserDao systemUserDAO;

      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
          return true;
        }

        String accessToken = TokenUtils.getRequestToken(request);
        if (StringUtils.isEmpty(accessToken)) {
          throw new CommonException(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, "无Token认证失败，请登录");
        }

        Object cache = CacheUtils.get(accessToken);
        if (null == cache) {
          throw new CommonException(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, "token不存在或已经失效，请重新登录!");
        }

        SystemUserEntity systemUserEntity = (SystemUserEntity) cache;
        SystemUserEntity user = systemUserDAO.findByUsername(systemUserEntity.getUsername());
        if (null == user) {
          throw new CommonException(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, "token所使用的认证用户不存在!");
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
          throw new CommonException(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, "token所使用的认证用户已经被锁定");
        }

        request.setAttribute("username", user.getUsername());
        return true;
      }
    };
  }

}