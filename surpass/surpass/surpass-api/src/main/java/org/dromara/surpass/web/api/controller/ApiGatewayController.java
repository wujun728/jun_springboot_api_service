package org.dromara.surpass.web.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dromara.surpass.entity.ApiRequestUri;
import org.dromara.surpass.persistence.service.ApiExecuteService;
import org.dromara.surpass.web.ResponseTemplateRenderer;
import org.dromara.surpass.web.WebContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一 OPEN API 网关解析和执行
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGatewayController {

    private final ApiExecuteService apiExecuteService;

    private final ResponseTemplateRenderer responseRenderer;

    /**
     * 通用 GET 请求
     */
    @GetMapping("/**")
    public ResponseEntity<Object> getHandle(
            HttpServletRequest request,
            @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.GET, request, combineParam(null, paramMap));
    }

    /**
     * 通用 POST 请求
     */
    @PostMapping("/**")
    public ResponseEntity<Object> postHandle(
            HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> bodyMap,
            @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.POST, request, combineParam(bodyMap, paramMap));
    }

    /**
     * 通用 PUT 请求
     */
    @PutMapping("/**")
    public ResponseEntity<Object> putHandle(
            HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> bodyMap,
            @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.PUT, request, combineParam(bodyMap, paramMap));
    }

    /**
     * 通用 DELETE 请求
     */
    @DeleteMapping("/**")
    public ResponseEntity<Object> deleteHandle(
            HttpServletRequest request,
            @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.DELETE, request, combineParam(null, paramMap));
    }

    /**
     * 请求参数合并
     *
     * @param bodyMap
     * @param paramMap
     * @return
     */
    Map<String, Object> combineParam(Map<String, Object> bodyMap, Map<String, Object> paramMap) {
        Map<String, Object> combinedParamMap = new HashMap<>();
        if (paramMap != null) {
            combinedParamMap.putAll(paramMap);
        }
        if (bodyMap != null) {
            combinedParamMap.putAll(bodyMap);
        }
        return combinedParamMap;
    }

    /**
     * 请求处理
     *
     * @param method
     * @param request
     * @param paramMap
     * @return
     */
    private ResponseEntity<Object> handleRequest(
            RequestMethod method,
            HttpServletRequest request,
            Map<String, Object> paramMap) {
        try {
            // 1.获取 URL 上下文
            ApiRequestUri apiRequestUri = WebContext.explainRequestUri(request);
            // 2. 执行 API
            Object response = apiExecuteService.execute(apiRequestUri, method.name(), paramMap, false);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            log.error("API 网关处理失败：{} {}", method, request.getRequestURI(), e);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseRenderer.renderErrorResponse("API 执行失败：" + e.getMessage()));
        }
    }

}
