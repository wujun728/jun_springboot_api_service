package com.ruoyi.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 网关鉴权过滤器
 * 验证JWT Token合法性
 *
 * @author ruoyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${token.secret:abcdefghijklmnopqrstuvwxyz}")
    private String secret;

    /** 不需要认证的路径 */
    private static final List<String> SKIP_URLS = Arrays.asList(
            "/login", "/logout", "/refresh",
            "/captchaImage", "/register",
            "/v2/api-docs", "/swagger-resources",
            "/actuator/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 跳过不需要认证的路径
        if (isSkipUrl(path)) {
            return chain.filter(exchange);
        }

        String token = getToken(request);
        if (token == null || token.isEmpty()) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        try {
            Claims claims = parseToken(token);
            String userId = claims.get("user_id", String.class);
            String username = claims.get("username", String.class);
            String userKey = claims.get("user_key", String.class);

            // 设置用户信息到请求头
            ServerHttpRequest mutableRequest = request.mutate()
                    .header("user_id", userId != null ? userId : "")
                    .header("username", username != null ? username : "")
                    .header("user_key", userKey != null ? userKey : "")
                    // 清除来源标识，防止外部伪造内部请求
                    .headers(httpHeaders -> httpHeaders.remove("from-source"))
                    .build();

            return chain.filter(exchange.mutate().request(mutableRequest).build());
        } catch (Exception e) {
            log.error("令牌验证失败: {}", e.getMessage());
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
    }

    /**
     * 获取请求Token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }

    /**
     * 解析JWT令牌
     */
    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 判断是否跳过认证
     */
    private boolean isSkipUrl(String path) {
        return SKIP_URLS.stream().anyMatch(pattern -> {
            if (pattern.endsWith("/**")) {
                return path.startsWith(pattern.substring(0, pattern.length() - 3));
            }
            return path.equals(pattern);
        });
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"code\":401,\"msg\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
