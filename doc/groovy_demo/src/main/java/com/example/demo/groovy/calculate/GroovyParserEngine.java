package com.example.demo.groovy.calculate;

import com.example.demo.entity.request.CalculateRequest;
import com.example.demo.entity.response.CalculateResponse;

public interface GroovyParserEngine {

    CalculateResponse parse(CalculateRequest request);
}
