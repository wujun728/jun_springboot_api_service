package com.gitee.freakchicken.dbapi.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public abstract class ParameterValidator implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(ParameterValidator.class);

    public abstract void init();

    public abstract boolean validate(Map<String, Object> requestParameter, ApiConfig apiConfig, String clientId, String localPluginParam);
}
