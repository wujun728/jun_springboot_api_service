package com.example.demo.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class CalculateResponse implements Serializable {

    private String interfaceId;

    private Map<String, Object> extendInfo;
}
