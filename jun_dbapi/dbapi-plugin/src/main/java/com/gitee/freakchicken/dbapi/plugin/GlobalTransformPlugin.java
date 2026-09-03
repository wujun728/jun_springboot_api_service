package com.gitee.freakchicken.dbapi.plugin;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GlobalTransformPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(GlobalTransformPlugin.class);

    public abstract void init();

    public abstract Object transform(ResponseDto data, String localPluginParam);
}
