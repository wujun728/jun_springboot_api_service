package com.ruoyi.api.aggregate.service;

import java.util.*;

public class DataTransformer {

    public static Map<String, Object> kvConver(Map<String, Object> map, String kvConverStr) {
        Map<String, String> config = parseJsonToMap(kvConverStr);
        Map<String, Object> result = new HashMap<>();
        if (config.containsKey("name") && map.containsKey(config.get("name"))
                && config.containsKey("value") && map.containsKey(config.get("value"))) {
            result.put("name", map.get(config.get("name")));
            result.put("value", map.get(config.get("value")));
        }
        return result;
    }

    public static List<Map<String, Object>> kvConver(List<Map<String, Object>> list, String kvConverStr) {
        Map<String, String> config = parseJsonToMap(kvConverStr);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : list) {
            Map<String, Object> item = new HashMap<>();
            if (config.containsKey("name") && row.containsKey(config.get("name"))
                    && config.containsKey("value") && row.containsKey(config.get("value"))) {
                item.put("name", row.get(config.get("name")));
                item.put("value", row.get(config.get("value")));
            }
            result.add(item);
        }
        return result;
    }

    public static Map<String, Object> rcConver(Map<String, Object> map, String rcConverStr) {
        Map<String, String> config = parseJsonToMap(rcConverStr);
        Map<String, Object> result = new HashMap<>();
        if (config.containsKey("name") && map.containsKey(config.get("name"))
                && config.containsKey("value") && map.containsKey(config.get("value"))) {
            result.put(String.valueOf(map.get(config.get("name"))), map.get(config.get("value")));
        }
        return result;
    }

    public static Map<String, Object> rcConver(List<Map<String, Object>> list, String rcConverStr) {
        Map<String, String> config = parseJsonToMap(rcConverStr);
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> row : list) {
            if (config.containsKey("name") && row.containsKey(config.get("name"))
                    && config.containsKey("value") && row.containsKey(config.get("value"))) {
                result.put(String.valueOf(row.get(config.get("name"))), row.get(config.get("value")));
            }
        }
        return result;
    }

    public static Map<String, Object> keyExtract(Map<String, Object> map, String keyExtractStr) {
        Map<String, String> config = parseJsonToMap(keyExtractStr);
        String key = config.getOrDefault("key", "");
        Map<String, Object> result = new HashMap<>();
        if (map.containsKey(key)) {
            result.put(String.valueOf(map.get(key)), map);
        }
        return result;
    }

    public static List<Map<String, Object>> keyExtract(List<Map<String, Object>> list, String keyExtractStr) {
        Map<String, String> config = parseJsonToMap(keyExtractStr);
        String key = config.getOrDefault("key", "");
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : list) {
            if (row.containsKey(key)) {
                Map<String, Object> item = new HashMap<>();
                item.put(String.valueOf(row.get(key)), row);
                result.add(item);
            }
        }
        return result;
    }

    private static Map<String, String> parseJsonToMap(String json) {
        try {
            return com.alibaba.fastjson2.JSONObject.parseObject(json, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}