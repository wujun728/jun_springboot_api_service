package com.gitee.freakchicken.dbapi.conf;

import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PluginInitializer {

    @PostConstruct
    public void init() {
        log.info("开始加载插件...");
        PluginManager.loadPlugins();
        log.info("插件加载完成");
    }
}
