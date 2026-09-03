package com.gitee.freakchicken.dbapi.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public abstract class ParamProcessPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(ParamProcessPlugin.class);

    public abstract void init();

    public abstract void process(Map<String, Object> requestParam, ApiConfig apiConfig, String localPluginParam);
}
