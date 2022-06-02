/*
 * @Copyright:  2019 dxxld@qq.com Personal.
 * All Rights Reserved.
 */

package com.wauil.groovy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.script.*;
import java.util.HashMap;

/**
 * @Description:
 * @Author: Bin.Zhou
 * @Email: dxxld@qq.com
 * @Date: 2019/01/30 15:48
 * @Version: 1.0
 */
@RestController
@Api(tags = "Groovy脚本执行接口")
@RequestMapping(path = "/groovy")
public class GroovyScriptController {
    private ScriptEngineManager scriptEngineManager;
    private ApplicationContext applicationContext;

    @Autowired
    public GroovyScriptController(ScriptEngineManager scriptEngineManager, ApplicationContext applicationContext) {
        Assert.notNull(scriptEngineManager, "scriptEngineManager is not allowed null.");
        Assert.notNull(applicationContext, "applicationContext is not allowed null.");
        this.scriptEngineManager = scriptEngineManager;
        this.applicationContext = applicationContext;
    }

    @PostMapping
    @ApiOperation(notes = "执行Groovy脚本", value = "执行groovy脚本")
    public Object execute(String script) throws ScriptException {
        ScriptEngine engine = scriptEngineManager.getEngineByName("groovy");
        ScriptContext context = new SimpleScriptContext();
        context.setBindings(new SimpleBindings(new HashMap<String, Object>(1) {{
            put("spring", applicationContext);
        }}), ScriptContext.ENGINE_SCOPE);
        return engine.eval(script, context).toString();
    }
}
