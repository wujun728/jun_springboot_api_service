package com.github.alenfive.rocketopenapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.alenfive.rocketapi.extend.ApiInfoContent;
import com.github.alenfive.rocketopenapi.entity.RouteCacheEntity;
import com.github.alenfive.rocketopenapi.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class RouteService {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private RequestUtils requestUtils;
    private ApiInfoContent apiInfoContent;
    private RedisTemplate redisTemplate;

    private Long cacheTime;
    private Long limiterTime;
    private Long limiter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private String requestUri;
    private String requestMethod;
    private Map<String, String> requestHeader;
    private Map<String, Object> requestBody;

    private Map<String, String> responseHeader;
    private Object responseBody;

    public RouteService(RestTemplate restTemplate, HttpServletRequest request, HttpServletResponse response, ObjectMapper objectMapper, RequestUtils requestUtils,
                        ApiInfoContent apiInfoContent, RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.request = request;
        this.response = response;
        this.objectMapper = objectMapper;
        this.requestUtils = requestUtils;
        this.apiInfoContent = apiInfoContent;
        this.redisTemplate = redisTemplate;

        //request data
        this.requestMethod = request.getMethod();
        this.requestUri = request.getRequestURI();
        this.requestHeader = requestUtils.buildHeaderParams(request);
        this.requestBody = (Map<String, Object>) apiInfoContent.getEngineBindings().get("bodyRoot");

        //不转发压缩信息:会导致三方的返回不能在本程序得到解析
        this.requestHeader.remove("accept-encoding");
    }

    public RouteService cacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }

    public RouteService limiter(Long limiterTime, Long limiter) {
        this.limiterTime = limiterTime;
        this.limiter = limiter;
        return this;
    }

    public RouteService requestHeader(Function<Map<String, String>, Map<String, String>> function) {
        this.requestHeader = function.apply(requestHeader);
        return this;
    }

    public RouteService requestBody(Function<Map<String, Object>, Map<String, Object>> function) {
        this.requestBody = function.apply(requestBody);
        return this;
    }

    public RouteService requestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public RouteService to(String url) {

        //限流
        if (this.limiter != null) {
            String limiterKey = "openapi:limiter:" + requestMethod + "-" + requestUri;
            long times = redisTemplate.opsForValue().increment(limiterKey, 1);
            if (times > limiter) {
                throw new RuntimeException("访问过于频繁");
            }
            if (times == 1) {
                redisTemplate.expire(limiterKey, limiterTime, TimeUnit.SECONDS);
            }
        }

        //缓存
        String cacheKey = "openapi:cache:" + requestMethod + "-" + requestUri;
        if (this.cacheTime != null) {
            RouteCacheEntity cacheValue = (RouteCacheEntity) redisTemplate.opsForValue().get(cacheKey);
            if (cacheValue != null) {
                this.responseHeader = cacheValue.getResponseHeader();
                this.responseBody = cacheValue.getResponseBody();
                return this;
            }
        }

        //构建请求
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(requestHeader);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.valueOf(requestMethod), httpEntity, String.class);
        this.responseHeader = RequestUtils.buildHeaderParams(result);
        try {
            this.responseBody = objectMapper.readValue(result.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        //缓存数据
        if (this.cacheTime != null) {
            RouteCacheEntity cacheValue = RouteCacheEntity.builder()
                    .responseHeader(this.responseHeader)
                    .responseBody(this.responseBody)
                    .build();
            redisTemplate.opsForValue().set(cacheKey, cacheValue, cacheTime, TimeUnit.SECONDS);
        }

        return this;
    }

    public RouteService responseHeader(Function<Map<String, String>, Map<String, String>> function) {
        this.responseHeader = function.apply(this.responseHeader);
        return this;
    }

    public RouteService responseBody(Function<Object, Object> function) {
        this.responseBody = function.apply(this.responseBody);
        return this;
    }

    public Object done() {

        //移除上游的连接状态-Connection=close,nginx接收到该header会返回502 http状态
        this.responseHeader.remove("Connection");

        this.responseHeader.forEach((key, value) -> {
            this.response.setHeader(key, value);
        });

        return this.responseBody;
    }

}
