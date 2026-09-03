package com.github.alenfive.rocketapi.extend;

import com.github.alenfive.rocketapi.entity.ApiInfo;
import com.github.alenfive.rocketapi.entity.ApiParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRequestInterceptor {
    public abstract boolean preHandle(HttpServletRequest request, HttpServletResponse response, ApiInfo apiInfo, ApiParams apiParams);
}
