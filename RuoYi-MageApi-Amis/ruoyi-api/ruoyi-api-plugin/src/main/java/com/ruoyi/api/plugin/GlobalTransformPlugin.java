package com.ruoyi.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GlobalTransformPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(GlobalTransformPlugin.class);

    public abstract void init();

    public abstract Object transform(Object data, String localPluginParam);
}