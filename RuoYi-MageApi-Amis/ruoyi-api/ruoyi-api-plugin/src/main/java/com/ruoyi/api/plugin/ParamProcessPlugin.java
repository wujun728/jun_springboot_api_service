package com.ruoyi.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public abstract class ParamProcessPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(ParamProcessPlugin.class);

    public abstract void init();

    public abstract void process(Map<String, Object> requestParam, String apiId, String localPluginParam);
}