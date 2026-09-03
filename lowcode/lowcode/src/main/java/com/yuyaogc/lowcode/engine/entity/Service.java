package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.exception.EngineException;
import groovy.lang.GroovyClassLoader;

/**
 * 服务
 */
public class Service extends Entity{
    private String name;
    private String displayName;
    private GroovyClassLoader classLoader = new GroovyClassLoader();
    private String script;
    private BaseService baseService;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void exe() {
        if (baseService == null) {
            try {
                baseService = (BaseService) classLoader.parseClass(script).getConstructor().newInstance();
            } catch (Exception e) {
                throw new EngineException(String.format("Service script[%s] 构建服务错误", script), e);
            }
        }
    }

}
