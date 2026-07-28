package com.ruoyi.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AlarmPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(AlarmPlugin.class);

    public abstract void init();

    public abstract void alarm(Exception e, String apiPath, String requestUri, String localPluginParam);
}