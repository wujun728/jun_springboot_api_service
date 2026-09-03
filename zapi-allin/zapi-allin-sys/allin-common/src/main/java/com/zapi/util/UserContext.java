package com.zapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户上下文工具类
 * 提供统一的获取当前登录用户信息的方法
 * 
 * 使用方式：
 * 1. 获取用户ID: UserContext.getCurrentUserId()
 * 2. 获取用户ID字符串: UserContext.getCurrentUserIdStr()
 * 3. 获取用户名: UserContext.getCurrentUsername()
 * 
 * @author zapi
 */
public final class UserContext {
    
    private static final Logger log = LoggerFactory.getLogger(UserContext.class);
    
    private UserContext() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * 获取当前登录用户ID
     * 优先从 Spring Security 获取，其次尝试 Sa-Token
     * 
     * @return 用户ID，未登录返回 null
     */
    public static Long getCurrentUserId() {
        // 1. 优先从 Spring Security 获取
        Long userId = getFromSpringSecurity();
        if (userId != null) {
            return userId;
        }
        
        // 2. 尝试从 Sa-Token 获取
        userId = getFromSaToken();
        if (userId != null) {
            return userId;
        }
        
        log.debug("无法获取当前登录用户ID");
        return null;
    }
    
    /**
     * 获取当前登录用户ID（字符串形式）
     * 
     * @return 用户ID字符串，未登录返回 null
     */
    public static String getCurrentUserIdStr() {
        Long userId = getCurrentUserId();
        return userId != null ? String.valueOf(userId) : null;
    }
    
    /**
     * 获取当前登录用户ID，如果未登录则抛出异常
     * 
     * @return 用户ID
     * @throws IllegalStateException 如果用户未登录
     */
    public static Long requireCurrentUserId() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("用户未登录，请先登录");
        }
        return userId;
    }
    
    /**
     * 获取当前登录用户ID字符串，如果未登录则抛出异常
     * 
     * @return 用户ID字符串
     * @throws IllegalStateException 如果用户未登录
     */
    public static String requireCurrentUserIdStr() {
        return String.valueOf(requireCurrentUserId());
    }
    
    /**
     * 获取当前登录用户名
     * 
     * @return 用户名，未登录返回 null
     */
    public static String getCurrentUsername() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object securityContext = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = securityContext.getClass().getMethod("getAuthentication").invoke(securityContext);
            
            if (authentication != null) {
                Boolean isAuthenticated = (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
                if (Boolean.TRUE.equals(isAuthenticated)) {
                    Object principal = authentication.getClass().getMethod("getPrincipal").invoke(authentication);
                    if (principal instanceof String) {
                        return (String) principal;
                    }
                    return (String) authentication.getClass().getMethod("getName").invoke(authentication);
                }
            }
        } catch (ClassNotFoundException ignore) {
            // Spring Security 未引入
        } catch (Throwable t) {
            log.debug("从 Spring Security 获取用户名失败: {}", t.getMessage());
        }
        return null;
    }
    
    /**
     * 检查当前用户是否已登录
     * 
     * @return true 已登录，false 未登录
     */
    public static boolean isLoggedIn() {
        return getCurrentUserId() != null;
    }
    
    /**
     * 从 Spring Security 获取用户ID（通过反射，避免硬编码依赖）
     */
    private static Long getFromSpringSecurity() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object securityContext = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = securityContext.getClass().getMethod("getAuthentication").invoke(securityContext);
            
            if (authentication != null) {
                Boolean isAuthenticated = (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
                if (Boolean.TRUE.equals(isAuthenticated)) {
                    // 尝试从 details 获取
                    Object details = authentication.getClass().getMethod("getDetails").invoke(authentication);
                    if (details instanceof Long) {
                        return (Long) details;
                    }
                    if (details instanceof String) {
                        try {
                            return Long.parseLong((String) details);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    
                    // 尝试从 principal 获取
                    Object principal = authentication.getClass().getMethod("getPrincipal").invoke(authentication);
                    if (principal instanceof Long) {
                        return (Long) principal;
                    }
                    if (principal instanceof String) {
                        try {
                            return Long.parseLong((String) principal);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ignore) {
            // Spring Security 未引入，忽略
        } catch (Throwable t) {
            log.debug("从 Spring Security 获取用户ID失败: {}", t.getMessage());
        }
        return null;
    }
    
    /**
     * 从 Sa-Token 获取用户ID（通过反射，避免硬编码依赖）
     */
    private static Long getFromSaToken() {
        try {
            Class<?> stpClass = Class.forName("cn.dev33.satoken.stp.StpUtil");
            boolean isLogin = (boolean) stpClass.getMethod("isLogin").invoke(null);
            if (isLogin) {
                Object loginId = stpClass.getMethod("getLoginIdAsLong").invoke(null);
                if (loginId instanceof Long) {
                    return (Long) loginId;
                }
            }
        } catch (ClassNotFoundException ignore) {
            // 未引入 Sa-Token，忽略
        } catch (Throwable t) {
            log.debug("从 Sa-Token 获取用户ID失败: {}", t.getMessage());
        }
        return null;
    }
}
