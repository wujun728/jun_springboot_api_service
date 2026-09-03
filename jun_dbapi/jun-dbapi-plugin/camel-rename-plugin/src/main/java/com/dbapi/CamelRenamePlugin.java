package com.dbapi;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CamelRenamePlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String localPluginParam) {
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("CamelRenamePlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
        }
        List<JSONObject> list = (List<JSONObject>) data;
        List<JSONObject> result = new ArrayList<>();
        for (JSONObject row : list) {
            JSONObject newRow = new JSONObject(true);
            Set<String> keys = row.keySet();
            for (String key : keys) {
                newRow.put(underscoreToCamel(key), row.get(key));
            }
            result.add(newRow);
        }
        return result;
    }

    private String underscoreToCamel(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else {
                if (upperNext) {
                    sb.append(Character.toUpperCase(c));
                    upperNext = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return "驼峰命名转换插件";
    }

    @Override
    public String getDescription() {
        return "将SQL查询结果的列名从下划线命名(snake_case)转换为驼峰命名(camelCase)";
    }

    @Override
    public String getParamDescription() {
        return "不需要填参数";
    }
}
