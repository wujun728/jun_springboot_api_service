package org.dromara.surpass.proxy.dto;

import lombok.Data;

/**
 * HTTP 代理请求
 */
@Data
public class ProxyRequest {

    /**
     * 目标 URL
     */
    private String targetUrl;

    /**
     * 请求方法
     */
    private String method = "GET";

    /**
     * 协议类型 (api_key, oauth2, jwt, basic_auth, bearer_token, digest_auth, custom)
     */
    private String protocolType;

    /**
     * 配置 JSON
     */
    private String configJson;

    /**
     * 请求头 (JSON 格式)
     */
    private String headers;

    /**
     * 请求体
     */
    private String body;

    /**
     * 查询参数 (JSON 格式)
     */
    private String queryParams;

    /**
     * 超时时间 (毫秒)
     */
    private Integer timeout = 30000;

    /**
     * 是否跟随重定向
     */
    private Boolean followRedirects = true;
}
