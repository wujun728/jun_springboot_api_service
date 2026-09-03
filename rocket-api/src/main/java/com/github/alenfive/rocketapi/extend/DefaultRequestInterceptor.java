package com.github.alenfive.rocketapi.extend;

import com.github.alenfive.rocketapi.entity.ApiInfo;
import com.github.alenfive.rocketapi.entity.ApiParams;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认结果包装类
 */
@Component
public class DefaultRequestInterceptor implements IRequestInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, ApiInfo apiInfo, ApiParams apiParams) {
        return true;
    }
}
