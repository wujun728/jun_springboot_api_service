package com.github.alenfive.rocketopenapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RouteCacheEntity implements Serializable {
    private Map<String, String> responseHeader;
    private Object responseBody;
}
