package com.ruoyi.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PluginManager {

    private static final Logger log = LoggerFactory.getLogger(PluginManager.class);

    private static final Map<String, CachePlugin> cachePluginMap = new ConcurrentHashMap<>();
    private static final Map<String, TransformPlugin> transformPluginMap = new ConcurrentHashMap<>();
    private static final Map<String, GlobalTransformPlugin> globalTransformPluginMap = new ConcurrentHashMap<>();
    private static final Map<String, AlarmPlugin> alarmPluginMap = new ConcurrentHashMap<>();
    private static final Map<String, ParamProcessPlugin> paramProcessPluginMap = new ConcurrentHashMap<>();
    private static final Map<String, ParameterValidator> parameterValidatorMap = new ConcurrentHashMap<>();

    public static void loadPlugins() {
        cachePluginMap.clear();
        transformPluginMap.clear();
        globalTransformPluginMap.clear();
        alarmPluginMap.clear();
        paramProcessPluginMap.clear();
        parameterValidatorMap.clear();

        ServiceLoader<CachePlugin> cacheLoader = ServiceLoader.load(CachePlugin.class);
        for (CachePlugin plugin : cacheLoader) {
            plugin.init();
            log.info("CachePlugin registered: {}", plugin.getClass().getName());
            cachePluginMap.put(plugin.getClass().getName(), plugin);
        }

        ServiceLoader<TransformPlugin> transformLoader = ServiceLoader.load(TransformPlugin.class);
        for (TransformPlugin plugin : transformLoader) {
            plugin.init();
            log.info("TransformPlugin registered: {}", plugin.getClass().getName());
            transformPluginMap.put(plugin.getClass().getName(), plugin);
        }

        ServiceLoader<AlarmPlugin> alarmLoader = ServiceLoader.load(AlarmPlugin.class);
        for (AlarmPlugin plugin : alarmLoader) {
            plugin.init();
            log.info("AlarmPlugin registered: {}", plugin.getClass().getName());
            alarmPluginMap.put(plugin.getClass().getName(), plugin);
        }

        ServiceLoader<GlobalTransformPlugin> globalLoader = ServiceLoader.load(GlobalTransformPlugin.class);
        for (GlobalTransformPlugin plugin : globalLoader) {
            plugin.init();
            log.info("GlobalTransformPlugin registered: {}", plugin.getClass().getName());
            globalTransformPluginMap.put(plugin.getClass().getName(), plugin);
        }

        ServiceLoader<ParamProcessPlugin> paramLoader = ServiceLoader.load(ParamProcessPlugin.class);
        for (ParamProcessPlugin plugin : paramLoader) {
            plugin.init();
            log.info("ParamProcessPlugin registered: {}", plugin.getClass().getName());
            paramProcessPluginMap.put(plugin.getClass().getName(), plugin);
        }

        ServiceLoader<ParameterValidator> validatorLoader = ServiceLoader.load(ParameterValidator.class);
        for (ParameterValidator plugin : validatorLoader) {
            plugin.init();
            log.info("ParameterValidator registered: {}", plugin.getClass().getName());
            parameterValidatorMap.put(plugin.getClass().getName(), plugin);
        }
    }

    public static CachePlugin getCachePlugin(String className) {
        if (!cachePluginMap.containsKey(className)) {
            throw new RuntimeException("CachePlugin not found: " + className);
        }
        return cachePluginMap.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        if (!transformPluginMap.containsKey(className)) {
            throw new RuntimeException("TransformPlugin not found: " + className);
        }
        return transformPluginMap.get(className);
    }

    public static GlobalTransformPlugin getGlobalTransformPlugin(String className) {
        if (!globalTransformPluginMap.containsKey(className)) {
            throw new RuntimeException("GlobalTransformPlugin not found: " + className);
        }
        return globalTransformPluginMap.get(className);
    }

    public static AlarmPlugin getAlarmPlugin(String className) {
        if (!alarmPluginMap.containsKey(className)) {
            throw new RuntimeException("AlarmPlugin not found: " + className);
        }
        return alarmPluginMap.get(className);
    }

    public static ParamProcessPlugin getParamProcessPlugin(String className) {
        if (!paramProcessPluginMap.containsKey(className)) {
            throw new RuntimeException("ParamProcessPlugin not found: " + className);
        }
        return paramProcessPluginMap.get(className);
    }

    public static ParameterValidator getParameterValidator(String className) {
        if (!parameterValidatorMap.containsKey(className)) {
            throw new RuntimeException("ParameterValidator not found: " + className);
        }
        return parameterValidatorMap.get(className);
    }

    public static List<Map<String, String>> getAllPlugins(String type) {
        Map<String, ? extends BasePlugin> map;
        switch (type) {
            case "cache": map = cachePluginMap; break;
            case "transform": map = transformPluginMap; break;
            case "alarm": map = alarmPluginMap; break;
            case "global": map = globalTransformPluginMap; break;
            case "param": map = paramProcessPluginMap; break;
            case "validator": map = parameterValidatorMap; break;
            default: return Collections.emptyList();
        }
        return map.values().stream().map(p -> {
            Map<String, String> m = new HashMap<>();
            m.put("className", p.getClass().getName());
            m.put("name", p.getName());
            m.put("description", p.getDescription());
            m.put("paramDescription", p.getParamDescription());
            return m;
        }).collect(Collectors.toList());
    }
}