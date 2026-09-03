package com.gitee.freakchicken.dbapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/plugin")
public class PluginController {

    @RequestMapping("/all")
    public JSONObject getAllPlugins() {
        JSONObject result = new JSONObject();
        result.put("cachePlugins", PluginManager.getAllCachePlugins());
        result.put("transformPlugins", PluginManager.getAllTransformPlugins());
        result.put("globalTransformPlugins", PluginManager.getAllGlobalTransformPlugins());
        result.put("alarmPlugins", PluginManager.getAllAlarmPlugins());
        result.put("paramProcessPlugins", PluginManager.getAllParamProcessPlugins());
        result.put("parameterValidators", PluginManager.getAllParameterValidators());
        return result;
    }

    @RequestMapping("/cache")
    public List<JSONObject> getCachePlugins() {
        return PluginManager.getAllCachePlugins();
    }

    @RequestMapping("/transform")
    public List<JSONObject> getTransformPlugins() {
        return PluginManager.getAllTransformPlugins();
    }

    @RequestMapping("/globalTransform")
    public List<JSONObject> getGlobalTransformPlugins() {
        return PluginManager.getAllGlobalTransformPlugins();
    }

    @RequestMapping("/alarm")
    public List<JSONObject> getAlarmPlugins() {
        return PluginManager.getAllAlarmPlugins();
    }

    @RequestMapping("/paramProcess")
    public List<JSONObject> getParamProcessPlugins() {
        return PluginManager.getAllParamProcessPlugins();
    }

    @RequestMapping("/parameterValidator")
    public List<JSONObject> getParameterValidators() {
        return PluginManager.getAllParameterValidators();
    }
}
