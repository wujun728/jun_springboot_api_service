package com.zapi.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.annotation.LogOperation;
import com.zapi.event.LogEvent;
import com.zapi.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 日志切面类
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 定义切点 - 所有Controller类的方法
     * 支持项目中所有模块的controller包
     * 使用通配符匹配所有controller包，更加灵活
     */
    private static final String CONTROLLER_POINTCUT = 
        "execution(* com.zapi..controller.*.*(..)) || " +                   // 匹配所有模块的controller包
        "execution(* com.zapi.controller.*.*(..))";                        // 兼容system模块的controller包

    /**
     * 方法执行前记录日志
     */
    @Before(CONTROLLER_POINTCUT)
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            String fullClassName = joinPoint.getTarget().getClass().getName();
            String moduleName = getModuleName(fullClassName);
            
            log.info("=== [{} 模块] 进入方法: {}.{} ===", moduleName, className, methodName);
            log.info("请求URL: {}", request.getRequestURL().toString());
            log.info("请求方法: {}", request.getMethod());
            log.info("请求IP: {}", request.getRemoteAddr());
            log.info("请求参数: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    /**
     * 方法执行后记录日志
     */
    @AfterReturning(pointcut = CONTROLLER_POINTCUT, returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullClassName = joinPoint.getTarget().getClass().getName();
        String moduleName = getModuleName(fullClassName);
        
        log.info("=== [{} 模块] 方法结束: {}.{} ===", moduleName, className, methodName);
        log.info("返回结果: {}", result);
    }

    /**
     * 环绕通知 - 记录方法执行时间并保存到数据库
     */
    @Around(CONTROLLER_POINTCUT)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        String username = "anonymous";
        Long userId = null;
        String operation = "";
        String method = "";
        String params = "";
        String ip = "";
        String url = "";
        String httpMethod = "";
        String userAgent = "";
        
        if (attributes != null) {
            request = attributes.getRequest();
            url = request.getRequestURL().toString();
            httpMethod = request.getMethod();
            ip = getClientIpAddress(request);
            userAgent = request.getHeader("User-Agent");
            
            // 获取方法信息
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            String fullClassName = joinPoint.getTarget().getClass().getName();
            String moduleName = getModuleName(fullClassName);
            method = moduleName + ":" + className + "." + methodName;
            
            // 获取操作描述
            operation = getOperationDescription(joinPoint, className, methodName);
            
            // 获取请求参数
            try {
                params = objectMapper.writeValueAsString(joinPoint.getArgs());
                if (params.length() > 1000) {
                    params = params.substring(0, 1000) + "...";
                }
            } catch (Exception e) {
                params = Arrays.toString(joinPoint.getArgs());
            }
            
            // 获取当前用户信息
            try {
                // 从请求头获取Authorization
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7); // 去掉"Bearer "前缀
                    try {
                        username = jwtUtil.getUsernameFromToken(token);
                        userId = jwtUtil.getUserIdFromToken(token);
                    } catch (Exception e) {
                        log.warn("解析JWT token失败: {}", e.getMessage());
                        username = "unknown";
                        userId = null;
                    }
                } else {
                    // 从session中获取用户信息
                    Object userObj = request.getSession().getAttribute("user");
                    if (userObj != null) {
                        username = userObj.toString();
                        // session中可能没有用户ID，暂时设为null
                        userId = null;
                    }
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败: {}", e.getMessage());
                username = "unknown";
                userId = null;
            }
        }
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // 发布成功日志事件
            LogEvent logEvent = new LogEvent(username, userId, operation, method, params, 
                                           ip, url, httpMethod, duration, 1, null, userAgent);
            eventPublisher.publishEvent(logEvent);
            
            log.info("=== 方法执行完成: {}.{}, 耗时: {}ms ===", method, duration);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // 发布失败日志事件
            LogEvent logEvent = new LogEvent(username, userId, operation, method, params, 
                                           ip, url, httpMethod, duration, 0, e.getMessage(), userAgent);
            eventPublisher.publishEvent(logEvent);
            
            log.error("=== 方法执行异常: {}.{}, 耗时: {}ms, 异常信息: {} ===", 
                     method, duration, e.getMessage());
            throw e;
        }
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 根据类名和方法名获取操作描述
     */
    private String getOperationDescription(ProceedingJoinPoint joinPoint, String className, String methodName) {
        // 首先检查是否有LogOperation注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation logOperation = method.getAnnotation(LogOperation.class);
        
        if (logOperation != null && !logOperation.value().isEmpty()) {
            return logOperation.value();
        }
        
        // 如果没有注解或注解值为空，则使用默认映射
        // 这里可以根据类名和方法名映射到具体的操作描述
        // 也可以使用注解来标记操作描述
        if (className.contains("UserController")) {
            switch (methodName) {
                case "login": return "用户登录";
                case "getUserList": return "查询用户列表";
                case "addUser": return "添加用户";
                case "updateUser": return "更新用户";
                case "deleteUser": return "删除用户";
                default: return "用户管理操作";
            }
        } else if (className.contains("RoleController")) {
            switch (methodName) {
                case "getRoleList": return "查询角色列表";
                case "addRole": return "添加角色";
                case "updateRole": return "更新角色";
                case "deleteRole": return "删除角色";
                default: return "角色管理操作";
            }
        } else if (className.contains("MenuController")) {
            switch (methodName) {
                case "getUserMenus": return "获取用户菜单";
                case "getMenuList": return "查询菜单列表";
                case "addMenu": return "添加菜单";
                case "updateMenu": return "更新菜单";
                case "deleteMenu": return "删除菜单";
                default: return "菜单管理操作";
            }
        } else if (className.contains("SysLogController")) {
            switch (methodName) {
                case "getLogList": return "查询系统日志";
                case "deleteLog": return "删除日志";
                case "clearLogs": return "清空日志";
                default: return "日志管理操作";
            }
        } else if (className.contains("FileController")) {
            switch (methodName) {
                case "uploadFile": return "文件上传";
                case "downloadFile": return "文件下载";
                case "deleteFile": return "文件删除";
                case "getFileList": return "查询文件列表";
                default: return "文件管理操作";
            }
        } else if (className.contains("NotificationController")) {
            switch (methodName) {
                case "sendStationMessage": return "发送站内信";
                case "sendRealTimeMessage": return "发送实时消息";
                case "sendSms": return "发送短信";
                case "sendEmail": return "发送邮件";
                default: return "通知管理操作";
            }
        } else if (className.contains("MessageController")) {
            switch (methodName) {
                case "sendMessage": return "发送消息";
                case "getReceivedMessages": return "查询接收消息";
                case "markAsRead": return "标记消息已读";
                case "deleteMessage": return "删除消息";
                default: return "消息管理操作";
            }
        }
        
        return className + "." + methodName;
    }
    
    /**
     * 根据类的完整路径获取模块名
     */
    private String getModuleName(String fullClassName) {
        if (fullClassName.contains(".dataset.")) {
            return "DATASET";
        } else if (fullClassName.contains(".notification.")) {
            return "NOTIFICATION";
        } else if (fullClassName.contains(".onlyoffice.")) {
            return "ONLYOFFICE";
        } else if (fullClassName.contains(".form.")) {
            return "FORM";
        } else if (fullClassName.contains(".file.")) {
            return "FILE";
        } else if (fullClassName.contains(".redis.")) {
            return "REDIS";
        } else if (fullClassName.contains(".workflow.")) {
            return "WORKFLOW";
        } else if (fullClassName.contains("com.zapi.controller")) {
            return "SYSTEM";
        } else {
            return "UNKNOWN";
        }
    }
} 