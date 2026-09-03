package com.zapi.openapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * HTTP客户端工具类
 * 用于主动调用外部接口
 */
@Slf4j
@Component
public class HttpClientUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HttpClientUtil() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @param headers 请求头（JSON格式字符串）
     * @param params 请求参数（JSON格式字符串）
     * @return 响应结果
     */
    public String sendGet(String url, String headers, String params) {
        try {
            // 解析请求头
            HttpHeaders httpHeaders = parseHeaders(headers);

            // 解析请求参数并拼接到URL
            String fullUrl = buildUrlWithParams(url, params);

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("GET请求成功: URL={}, 状态码={}", fullUrl, response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            log.error("GET请求失败: URL={}, 错误信息={}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP GET请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送POST请求
     *
     * @param url 请求URL
     * @param headers 请求头（JSON格式字符串）
     * @param params 请求参数（JSON格式字符串）
     * @return 响应结果
     */
    public String sendPost(String url, String headers, String params) {
        try {
            // 解析请求头
            HttpHeaders httpHeaders = parseHeaders(headers);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            // 构建请求体
            HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("POST请求成功: URL={}, 状态码={}", url, response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            log.error("POST请求失败: URL={}, 错误信息={}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP POST请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送PUT请求
     *
     * @param url 请求URL
     * @param headers 请求头（JSON格式字符串）
     * @param params 请求参数（JSON格式字符串）
     * @return 响应结果
     */
    public String sendPut(String url, String headers, String params) {
        try {
            HttpHeaders httpHeaders = parseHeaders(headers);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            log.info("PUT请求成功: URL={}, 状态码={}", url, response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            log.error("PUT请求失败: URL={}, 错误信息={}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP PUT请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送DELETE请求
     *
     * @param url 请求URL
     * @param headers 请求头（JSON格式字符串）
     * @param params 请求参数（JSON格式字符串）
     * @return 响应结果
     */
    public String sendDelete(String url, String headers, String params) {
        try {
            HttpHeaders httpHeaders = parseHeaders(headers);
            String fullUrl = buildUrlWithParams(url, params);

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );

            log.info("DELETE请求成功: URL={}, 状态码={}", url, response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            log.error("DELETE请求失败: URL={}, 错误信息={}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP DELETE请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析请求头JSON字符串
     */
    private HttpHeaders parseHeaders(String headersJson) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headersJson != null && !headersJson.trim().isEmpty()) {
            try {
                Map<String, String> headerMap = objectMapper.readValue(headersJson, Map.class);
                headerMap.forEach(httpHeaders::add);
            } catch (Exception e) {
                log.warn("解析请求头失败，使用默认请求头: {}", e.getMessage());
            }
        }
        return httpHeaders;
    }

    /**
     * 构建带参数的URL
     */
    private String buildUrlWithParams(String url, String paramsJson) {
        if (paramsJson == null || paramsJson.trim().isEmpty()) {
            return url;
        }

        try {
            Map<String, Object> paramMap = objectMapper.readValue(paramsJson, Map.class);
            StringBuilder urlBuilder = new StringBuilder(url);
            
            if (!paramMap.isEmpty()) {
                urlBuilder.append(url.contains("?") ? "&" : "?");
                paramMap.forEach((key, value) -> 
                    urlBuilder.append(key).append("=").append(value).append("&")
                );
                // 移除最后一个&
                urlBuilder.setLength(urlBuilder.length() - 1);
            }
            
            return urlBuilder.toString();
        } catch (Exception e) {
            log.warn("解析请求参数失败，使用原始URL: {}", e.getMessage());
            return url;
        }
    }

    /**
     * 通用HTTP请求方法
     *
     * @param method 请求方法（GET/POST/PUT/DELETE）
     * @param url 请求URL
     * @param headers 请求头（JSON格式字符串）
     * @param params 请求参数（JSON格式字符串）
     * @return 响应结果
     */
    public String sendRequest(String method, String url, String headers, String params) {
        if (method == null || method.trim().isEmpty()) {
            method = "GET";
        }

        switch (method.toUpperCase()) {
            case "POST":
                return sendPost(url, headers, params);
            case "PUT":
                return sendPut(url, headers, params);
            case "DELETE":
                return sendDelete(url, headers, params);
            case "GET":
            default:
                return sendGet(url, headers, params);
        }
    }
}