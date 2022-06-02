package com.example.demo.groovy.calculate;

import com.example.demo.entity.request.CalculateRequest;
import com.example.demo.entity.response.CalculateResponse;

/**
 * 接入方都要实现这个接口
 */
public interface CalculateParser {

    CalculateResponse parse(CalculateRequest request);
}
