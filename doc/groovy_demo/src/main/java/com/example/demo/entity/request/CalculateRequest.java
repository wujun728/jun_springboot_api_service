package com.example.demo.entity.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CalculateRequest implements Serializable {

    private String interfaceId;

    private Map<String, Object> extendInfo;
}
