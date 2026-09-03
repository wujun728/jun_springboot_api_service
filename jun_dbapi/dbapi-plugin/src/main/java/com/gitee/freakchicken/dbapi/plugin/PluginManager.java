package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class PluginManager {

    private static Map<String, BasePlugin> cachePluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> transformPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> globalTransformPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> alarmPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> paramProcessPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> parameterValidatorMap = new ConcurrentHashMap<>();

    private static List<JSONObject> allCachePlugins = new ArrayList<>();
    private static List<JSONObject> allTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allGlobalTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allAlarmPlugins = new ArrayList<>();
    private static List<JSONObject> allParamProcessPlugins = new ArrayList<>();
    private static List<JSONObject> allParameterValidators = new ArrayList<>();

    public static void loadPlugins() {
        cachePluginMap.clear();
        transformPluginMap.clear();
        globalTransformPluginMap.clear();
        alarmPluginMap.clear();
        paramProcessPluginMap.clear();
        parameterValidatorMap.clear();
        allCachePlugins.clear();
        allTransformPlugins.clear();
        allGlobalTransformPlugins.clear();
        allAlarmPlugins.clear();
        allParamProcessPlugins.clear();
        allParameterValidators.clear();

        ServiceLoader<CachePlugin> cacheLoader = ServiceLoader.load(CachePlugin.class);
        Iterator<CachePlugin> cacheIterator = cacheLoader.iterator();
        while (cacheIterator.hasNext()) {
            CachePlugin plugin = cacheIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            cachePluginMap.put(plugin.getClass().getName(), plugin);
        }
        allCachePlugins = getAllList(cachePluginMap);
        log.info("scan cache plugin finish");

        ServiceLoader<TransformPlugin> transformLoader = ServiceLoader.load(TransformPlugin.class);
        Iterator<TransformPlugin> transformIterator = transformLoader.iterator();
        while (transformIterator.hasNext()) {
            TransformPlugin plugin = transformIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            transformPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allTransformPlugins = getAllList(transformPluginMap);
        log.info("scan transform plugin finish");

        ServiceLoader<AlarmPlugin> alarmLoader = ServiceLoader.load(AlarmPlugin.class);
        Iterator<AlarmPlugin> alarmIterator = alarmLoader.iterator();
        while (alarmIterator.hasNext()) {
            AlarmPlugin plugin = alarmIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            alarmPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allAlarmPlugins = getAllList(alarmPluginMap);
        log.info("scan alarm plugin finish");

        ServiceLoader<GlobalTransformPlugin> globalLoader = ServiceLoader.load(GlobalTransformPlugin.class);
        Iterator<GlobalTransformPlugin> globalIterator = globalLoader.iterator();
        while (globalIterator.hasNext()) {
            GlobalTransformPlugin plugin = globalIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            globalTransformPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allGlobalTransformPlugins = getAllList(globalTransformPluginMap);
        log.info("scan global transform plugin finish");

        ServiceLoader<ParamProcessPlugin> paramProcessLoader = ServiceLoader.load(ParamProcessPlugin.class);
        Iterator<ParamProcessPlugin> paramProcessIterator = paramProcessLoader.iterator();
        while (paramProcessIterator.hasNext()) {
            ParamProcessPlugin plugin = paramProcessIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            paramProcessPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allParamProcessPlugins = getAllList(paramProcessPluginMap);
        log.info("scan param process plugin finish");

        ServiceLoader<ParameterValidator> validatorLoader = ServiceLoader.load(ParameterValidator.class);
        Iterator<ParameterValidator> validatorIterator = validatorLoader.iterator();
        while (validatorIterator.hasNext()) {
            ParameterValidator plugin = validatorIterator.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            parameterValidatorMap.put(plugin.getClass().getName(), plugin);
        }
        allParameterValidators = getAllList(parameterValidatorMap);
        log.info("scan validator plugin finish");
    }

    public static CachePlugin getCachePlugin(String className) {
        if (!cachePluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (CachePlugin) cachePluginMap.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        if (!transformPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (TransformPlugin) transformPluginMap.get(className);
    }

    public static GlobalTransformPlugin getGlobalTransformPlugin(String className) {
        if (!globalTransformPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (GlobalTransformPlugin) globalTransformPluginMap.get(className);
    }

    public static AlarmPlugin getAlarmPlugin(String className) {
        if (!alarmPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (AlarmPlugin) alarmPluginMap.get(className);
    }

    public static ParamProcessPlugin getParamProcessPlugin(String className) {
        if (!paramProcessPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (ParamProcessPlugin) paramProcessPluginMap.get(className);
    }

    public static ParameterValidator getParameterValidator(String className) {
        if (!parameterValidatorMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (ParameterValidator) parameterValidatorMap.get(className);
    }

    private static List<JSONObject> getAllList(Map<String, BasePlugin> map) {
        List<JSONObject> collect = map.values().stream().map(t -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("className", t.getClass().getName());
            jsonObject.put("name", t.getName());
            jsonObject.put("description", t.getDescription());
            jsonObject.put("paramDescription", t.getParamDescription());
            return jsonObject;
        }).collect(Collectors.toList());
        return collect;
    }

    public static List<JSONObject> getAllCachePlugins() {
        return allCachePlugins;
    }

    public static List<JSONObject> getAllAlarmPlugins() {
        return allAlarmPlugins;
    }

    public static List<JSONObject> getAllTransformPlugins() {
        return allTransformPlugins;
    }

    public static List<JSONObject> getAllGlobalTransformPlugins() {
        return allGlobalTransformPlugins;
    }

    public static List<JSONObject> getAllParamProcessPlugins() {
        return allParamProcessPlugins;
    }

    public static List<JSONObject> getAllParameterValidators() {
        return allParameterValidators;
    }
}
