/*
 * @Copyright:  2019 dxxld@qq.com Personal.
 * All Rights Reserved.
 */

package com.wauil.groovy.config;

import com.wauil.groovy.controller.GroovyScriptController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.script.ScriptEngineManager;

/**
 * @Description:
 * @Author: Bin.Zhou
 * @Email: dxxld@qq.com
 * @Date: 2019/01/30 15:48
 * @Version: 1.0
 */
@Configuration(value = "wauilGroovyJavaConfig")
@ConditionalOnProperty(prefix = "wauil.groovy", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(GroovyScriptController.class)
public class GroovyScriptAutoConfiguration {
    @Bean
    public ScriptEngineManager scriptEngineManager() {
        return new ScriptEngineManager();
    }
}
