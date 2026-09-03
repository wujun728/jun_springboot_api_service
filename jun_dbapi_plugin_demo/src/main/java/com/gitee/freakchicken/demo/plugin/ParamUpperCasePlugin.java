package com.gitee.freakchicken.demo.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;

import java.util.Map;

public class ParamUpperCasePlugin extends ParamProcessPlugin {
    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "参数值转大写插件";
    }

    @Override
    public String getDescription() {
        return "把参数值转成大写";
    }

    @Override
    public String getParamDescription() {
        return "无参数";
    }

    @Override
    public void process(Map<String, Object> requestParam, ApiConfig apiConfig, String localPluginParam) {
        requestParam.keySet().stream().forEach(key -> {
            requestParam.put(key, requestParam.get(key).toString().toUpperCase());
        });
    }
}
