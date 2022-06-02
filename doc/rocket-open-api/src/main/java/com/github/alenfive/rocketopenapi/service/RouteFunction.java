package com.github.alenfive.rocketopenapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alenfive.rocketapi.extend.ApiInfoContent;
import com.github.alenfive.rocketapi.function.IFunction;
import com.github.alenfive.rocketopenapi.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RouteFunction implements IFunction {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RequestUtils requestUtils;
    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String getVarName() {
        return "route";
    }

    public RouteService init() {
        return new RouteService(restTemplate, request, response, objectMapper, requestUtils, apiInfoContent, redisTemplate);
    }
}
