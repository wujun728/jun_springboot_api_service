package com.gitee.freakchicken.dbapi.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransformPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(TransformPlugin.class);

    /**
     * 插件初始化方法
     */
    public abstract void init();

    /**
     * 数据转换逻辑
     *
     * @param data             sql查询结果
     * @param localPluginParam 插件局部参数
     * @return
     */
    public abstract Object transform(Object data, String localPluginParam);
}
