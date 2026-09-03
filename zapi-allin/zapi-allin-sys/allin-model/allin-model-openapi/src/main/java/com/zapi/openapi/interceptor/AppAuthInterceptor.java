package com.zapi.openapi.interceptor;

import com.zapi.openapi.annotation.RequireAppAuth;
import com.zapi.openapi.entity.OpenApiConfig;
import com.zapi.openapi.service.OpenApiConfigService;
import com.zapi.workbench.entity.WbApplication;
import com.zapi.workbench.service.WbApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 应用认证拦截器
 * 拦截带有@RequireAppAuth注解的方法，验证应用认证信息和配置信息
 */
@Component
public class AppAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private WbApplicationService wbApplicationService;
    
    @Autowired
    private OpenApiConfigService openApiConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireAppAuth requireAppAuth = handlerMethod.getMethodAnnotation(RequireAppAuth.class);

        // 如果方法没有@RequireAppAuth注解，则直接放行
        if (requireAppAuth == null) {
            return true;
        }

        // 从请求头中获取认证信息
        String authCode = request.getHeader("X-Auth-Code");
        String authPsw = request.getHeader("X-Auth-Psw");

        // 如果认证信息为空且注解标记为必需，则返回错误
        if (requireAppAuth.required() && (authCode == null || authPsw == null || authCode.trim().isEmpty() || authPsw.trim().isEmpty())) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, requireAppAuth.message());
            return false;
        }

        // 验证应用认证信息
        WbApplication app = wbApplicationService.getApplicationByAuth(authCode, authPsw);
        if (app == null || app.getStatus() != 1) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, requireAppAuth.message());
            return false;
        }

        // 将应用信息存储到请求属性中
        request.setAttribute("currentApp", app);
        
        // 获取路径变量
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        // 如果包含configUuid路径变量，进行配置验证
        if (pathVariables != null && pathVariables.containsKey("configUuid")) {
            String configUuid = pathVariables.get("configUuid");
            String dataType = pathVariables.get("dataType");
            
            // 根据UUID获取配置
            OpenApiConfig config = openApiConfigService.getConfigByUuid(configUuid);
            if (config == null) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "配置不存在或已禁用");
                return false;
            }
            
            // 验证配置是否属于当前应用
            if (!config.getAppId().equals(app.getId())) {
                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "配置不属于当前应用");
                return false;
            }
            
            // 验证数据类型是否匹配
            if (dataType != null && !config.getDataType().equals(dataType)) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "数据类型不匹配");
                return false;
            }
            
            // 验证是否为被动模式
            if (config.getCallType() != 1) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "该配置不是被动模式");
                return false;
            }
            
            // 将配置信息存储到请求属性中
            request.setAttribute("currentConfig", config);
        }

        return true;
    }
    
    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":" + status + ",\"message\":\"" + message + "\"}");
    }
}