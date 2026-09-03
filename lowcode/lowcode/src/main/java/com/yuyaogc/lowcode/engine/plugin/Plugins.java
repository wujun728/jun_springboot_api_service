package com.yuyaogc.lowcode.engine.plugin;

import java.util.ArrayList;
import java.util.List;

final public class Plugins {

    private final List<IPlugin> pluginList = new ArrayList<IPlugin>();

    public Plugins add(IPlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin can not be null");
        }
        pluginList.add(plugin);
        return this;
    }

    public List<IPlugin> getPluginList() {
        return pluginList;
    }
}
