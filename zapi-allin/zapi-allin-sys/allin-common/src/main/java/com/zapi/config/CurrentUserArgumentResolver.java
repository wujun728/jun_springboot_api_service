package com.zapi.config;

import com.zapi.annotation.CurrentUser;
import com.zapi.util.UserContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 当前用户参数解析器
 * 用于解析 @CurrentUser 注解标记的方法参数
 * 
 * @author zapi
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        
        CurrentUser annotation = parameter.getParameterAnnotation(CurrentUser.class);
        Class<?> parameterType = parameter.getParameterType();
        
        // 获取当前用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 检查是否必须登录
        if (annotation != null && annotation.required() && userId == null) {
            throw new IllegalStateException("用户未登录，请先登录");
        }
        
        // 根据参数类型返回对应格式
        if (userId == null) {
            return null;
        }
        
        if (parameterType == Long.class || parameterType == long.class) {
            return userId;
        } else if (parameterType == String.class) {
            return String.valueOf(userId);
        } else if (parameterType == Integer.class || parameterType == int.class) {
            return userId.intValue();
        }
        
        return userId;
    }
}
