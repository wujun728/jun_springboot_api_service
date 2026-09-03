package org.dromara.surpass.proxy.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.dromara.surpass.proxy.dto.ProxyRequest;
import org.dromara.surpass.proxy.dto.ProxyResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP 代理客户端 - 支持多种认证方式
 * 纯工具类，无 Spring 依赖
 */
@Slf4j
public class HttpProxyClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // OAuth2 Token 缓存 (tokenKey -> tokenValue)
    private final Map<String, OAuth2TokenCache> tokenCache = new ConcurrentHashMap<>();

    /**
     * 执行代理请求
     */
    public ProxyResponse execute(ProxyRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 构建请求
            HttpUriRequestBase httpRequest = createRequest(request);

            // 应用认证
            applyAuthentication(httpRequest, request);

            // 添加自定义请求头
            if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
                JsonNode headers = objectMapper.readTree(request.getHeaders());
                headers.fields().forEachRemaining(entry -> {
                    httpRequest.setHeader(entry.getKey(), entry.getValue().asText());
                });
            }

            // 设置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(request.getTimeout(), java.util.concurrent.TimeUnit.MILLISECONDS)
                    .setResponseTimeout(request.getTimeout(), java.util.concurrent.TimeUnit.MILLISECONDS)
                    .setRedirectsEnabled(request.getFollowRedirects())
                    .build();
            httpRequest.setConfig(requestConfig);

            // 执行请求
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                    return parseResponse(response, startTime);
                }
            }
        } catch (Exception e) {
            log.error("代理请求执行失败：{}", request.getTargetUrl(), e);
            return ProxyResponse.error("请求执行失败：" + e.getMessage(), 500);
        }
    }

    /**
     * 创建 HTTP 请求对象
     */
    private HttpUriRequestBase createRequest(ProxyRequest request) throws Exception {
        String method = request.getMethod().toUpperCase();
        URI uri = new URI(request.getTargetUrl());

        HttpUriRequestBase httpRequest;
        switch (method) {
            case "POST":
                httpRequest = new HttpPost(uri);
                break;
            case "PUT":
                httpRequest = new HttpPut(uri);
                break;
            case "DELETE":
                httpRequest = new HttpDelete(uri);
                break;
            case "PATCH":
                httpRequest = new HttpPatch(uri);
                break;
            default:
                httpRequest = new HttpGet(uri);
        }

        // 设置请求体
        if (request.getBody() != null && !request.getBody().isEmpty() &&
                ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method))) {
            httpRequest.setEntity(new StringEntity(request.getBody(), ContentType.APPLICATION_JSON));
        }

        return httpRequest;
    }

    /**
     * 应用认证信息
     */
    private void applyAuthentication(HttpUriRequestBase request, ProxyRequest requestConfig) throws Exception {
        String protocolType = requestConfig.getProtocolType();
        String configJson = requestConfig.getConfigJson();

        if (configJson == null || configJson.isEmpty()) {
            return;
        }

        JsonNode configNode = objectMapper.readTree(configJson);

        switch (protocolType) {
            case "api_key":
                applyApiKeyAuth(request, configNode);
                break;
            case "oauth2":
                applyOAuth2Auth(request, configNode, requestConfig);
                break;
            case "jwt":
                applyJWTAuth(request, configNode);
                break;
            case "basic_auth":
                applyBasicAuth(request, configNode);
                break;
            case "bearer_token":
                applyBearerTokenAuth(request, configNode);
                break;
            case "digest_auth":
                applyDigestAuth(request, configNode);
                break;
            case "custom":
                applyCustomAuth(request, configNode);
                break;
            default:
                log.warn("未知的认证类型：{}", protocolType);
        }
    }

    /**
     * API Key 认证
     */
    private void applyApiKeyAuth(HttpUriRequestBase request, JsonNode config) throws URISyntaxException {
        String apiKey = config.path("apiKey").asText();
        String keyLocation = config.path("keyLocation").asText("header");
        String keyName = config.path("keyName").asText("X-API-Key");
        String prefix = config.path("prefix").asText("");

        if ("header".equals(keyLocation)) {
            request.setHeader(keyName, prefix + apiKey);
        } else if ("query".equals(keyLocation)) {
            appendQueryParam(request, keyName, prefix + apiKey);
        }
    }

    /**
     * OAuth2 认证
     */
    private void applyOAuth2Auth(HttpUriRequestBase request, JsonNode config, ProxyRequest proxyRequest) throws Exception {
        String clientId = config.path("clientId").asText();
        String clientSecret = config.path("clientSecret").asText();
        String grantType = config.path("grantType").asText("client_credentials");
        String tokenUrl = config.path("tokenUrl").asText();
        String scope = config.path("scope").asText("");
        String tokenLocation = config.path("tokenLocation").asText("header");
        String tokenPrefix = config.path("tokenPrefix").asText("Bearer ");

        // 获取或刷新 Token
        String accessToken = getOrRefreshToken(clientId, clientSecret, grantType, tokenUrl, scope);

        // 应用 Token
        if ("header".equals(tokenLocation)) {
            request.setHeader("Authorization", tokenPrefix + accessToken);
        } else if ("query".equals(tokenLocation)) {
            appendQueryParam(request, "access_token", accessToken);
        }
    }

    /**
     * 获取或刷新 OAuth2 Token
     */
    private String getOrRefreshToken(String clientId, String clientSecret, String grantType,
                                     String tokenUrl, String scope) throws Exception {
        String cacheKey = clientId + ":" + tokenUrl;

        // 检查缓存
        OAuth2TokenCache cached = tokenCache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            return cached.getToken();
        }

        // 请求新 Token
        String token = requestOAuth2Token(clientId, clientSecret, grantType, tokenUrl, scope);

        // 缓存 Token (默认缓存 55 分钟)
        tokenCache.put(cacheKey, new OAuth2TokenCache(token, 3300));

        return token;
    }

    /**
     * 请求 OAuth2 Token
     */
    private String requestOAuth2Token(String clientId, String clientSecret, String grantType,
                                      String tokenUrl, String scope) throws Exception {
        HttpPost post = new HttpPost(tokenUrl);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Basic Auth 编码
        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        post.setHeader("Authorization", "Basic " + credentials);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", grantType));
        if (scope != null && !scope.isEmpty()) {
            params.add(new BasicNameValuePair("scope", scope));
        }

        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                int statusCode = response.getCode();
                HttpEntity entity = response.getEntity();
                String responseBody = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";

                if (statusCode == 200) {
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    return jsonNode.path("access_token").asText();
                } else {
                    throw new RuntimeException("获取 OAuth2 Token 失败：" + statusCode + " - " + responseBody);
                }
            }
        }
    }

    /**
     * JWT 认证
     */
    private void applyJWTAuth(HttpUriRequestBase request, JsonNode config) throws URISyntaxException {
        String token = config.path("token").asText();
        String secret = config.path("secret").asText();
        String tokenLocation = config.path("tokenLocation").asText("header");
        String tokenName = config.path("tokenName").asText("Authorization");
        String tokenPrefix = config.path("tokenPrefix").asText("Bearer ");

        // 如果提供了 token 直接使用，否则使用 secret 生成
        String jwtToken = token;
        if (token.isEmpty() && !secret.isEmpty()) {
            jwtToken = generateJWT(secret, config.path("algorithm").asText("HS256"));
        }

        if ("header".equals(tokenLocation)) {
            request.setHeader(tokenName, tokenPrefix + jwtToken);
        } else if ("query".equals(tokenLocation)) {
            appendQueryParam(request, tokenName, tokenPrefix + jwtToken);
        }
    }

    /**
     * 生成简化 JWT
     */
    private String generateJWT(String secret, String algorithm) {
        String header = "{\"alg\":\"" + algorithm + "\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"proxy\",\"exp\":" + (System.currentTimeMillis() / 1000 + 3600) + "}";

        String headerEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String payloadEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // 简化实现，实际应该使用 nimbus-jose-jwt 库生成签名
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(
            (headerEncoded + "." + payloadEncoded + "." + secret).getBytes(StandardCharsets.UTF_8));

        return headerEncoded + "." + payloadEncoded + "." + signature;
    }

    /**
     * Basic Auth 认证
     */
    private void applyBasicAuth(HttpUriRequestBase request, JsonNode config) {
        String username = config.path("username").asText();
        String password = config.path("password").asText();
        String encoding = config.path("encoding").asText("base64");

        String credentials;
        if ("base64".equals(encoding)) {
            credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        } else {
            credentials = username + ":" + password;
        }

        request.setHeader("Authorization", "Basic " + credentials);
    }

    /**
     * Bearer Token 认证
     */
    private void applyBearerTokenAuth(HttpUriRequestBase request, JsonNode config) throws URISyntaxException {
        String token = config.path("token").asText();
        String tokenLocation = config.path("tokenLocation").asText("header");
        String tokenName = config.path("tokenName").asText("Authorization");

        if ("header".equals(tokenLocation)) {
            request.setHeader(tokenName, "Bearer " + token);
        } else if ("query".equals(tokenLocation)) {
            appendQueryParam(request, tokenName, token);
        }
    }

    /**
     * Digest Auth 认证 (简化实现)
     */
    private void applyDigestAuth(HttpUriRequestBase request, JsonNode config) {
        String username = config.path("username").asText();
        String password = config.path("password").asText();

        log.warn("Digest Auth 需要完整的挑战 - 响应流程，当前仅支持 Basic Auth 降级处理");
        // 降级为 Basic Auth
        applyBasicAuth(request, config);
    }

    /**
     * 自定义认证
     */
    private void applyCustomAuth(HttpUriRequestBase request, JsonNode config) throws Exception {
        // 从配置中读取自定义请求头
        config.fields().forEachRemaining(entry -> {
            request.setHeader(entry.getKey(), entry.getValue().asText());
        });
    }

    /**
     * 解析响应
     */
    private ProxyResponse parseResponse(CloseableHttpResponse response, long startTime) throws Exception {
        int statusCode = response.getCode();
        Map<String, String> headers = new HashMap<>();
        for (Header header : response.getHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        HttpEntity entity = response.getEntity();
        String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";

        long executionTime = System.currentTimeMillis() - startTime;

        return ProxyResponse.success(statusCode, headers, body, executionTime);
    }

    /**
     * 添加查询参数到请求
     */
    private void appendQueryParam(HttpUriRequestBase request, String name, String value) throws URISyntaxException {
        String uri = request.getUri().toString();
        String separator = uri.contains("?") ? "&" : "?";
        try {
            request.setUri(new URI(uri + separator + URLEncoder.encode(name, StandardCharsets.UTF_8) + "=" +
                    URLEncoder.encode(value, StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.warn("添加查询参数失败：{}", name, e);
        }
    }

    /**
     * OAuth2 Token 缓存类
     */
    private static class OAuth2TokenCache {
        private final String token;
        private final long ttlSeconds;
        private final long expireTime;

        public OAuth2TokenCache(String token, long ttlSeconds) {
            this.token = token;
            this.ttlSeconds = ttlSeconds;
            this.expireTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        public String getToken() {
            return token;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}
