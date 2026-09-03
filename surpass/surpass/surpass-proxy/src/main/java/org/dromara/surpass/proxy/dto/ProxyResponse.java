package org.dromara.surpass.proxy.dto;

import lombok.Data;

import java.util.Map;

/**
 * HTTP 代理响应
 */
@Data
public class ProxyResponse {

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 响应体
     */
    private String body;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 执行时间 (毫秒)
     */
    private Long executionTime;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误消息
     */
    private String errorMessage;

    public static ProxyResponse success(Integer statusCode, Map<String, String> headers, String body, Long executionTime) {
        ProxyResponse response = new ProxyResponse();
        response.setStatusCode(statusCode);
        response.setHeaders(headers);
        response.setBody(body);
        response.setExecutionTime(executionTime);
        response.setSuccess(true);
        return response;
    }

    public static ProxyResponse error(String errorMessage, Integer statusCode) {
        ProxyResponse response = new ProxyResponse();
        response.setErrorMessage(errorMessage);
        response.setStatusCode(statusCode);
        response.setSuccess(false);
        return response;
    }
}
