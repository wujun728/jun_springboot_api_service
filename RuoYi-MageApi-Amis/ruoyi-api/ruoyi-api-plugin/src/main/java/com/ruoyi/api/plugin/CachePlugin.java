package com.ruoyi.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class CachePlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(CachePlugin.class);

    public abstract void init();

    public abstract void set(String apiId, Map<String, Object> requestParams, Object data, String localPluginParam);

    public abstract void clean(String apiId, String localPluginParam);

    public abstract Object get(String apiId, Map<String, Object> requestParams, String localPluginParam);
}