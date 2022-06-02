package com.caojiulu.groovy.script.controller;

import com.caojiulu.groovy.script.domain.User;
import com.caojiulu.groovy.script.service.GroovyScriptService;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2020/5/6
 *
 * @author Jiulu Cao
 */
@RestController
@RequestMapping("/groovy/script")
public class GroovyScriptController {

    @Autowired
    private GroovyScriptService testService;

    @Autowired
    private GroovyShell groovyShell;


    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public Object execute(String scriptContent) {
        User user = new User();
        user.setUsername("caojiulu");
        user.setPassword("caojiulu"); //user 为具体的实例对象，例如ORGANIZATION("部门"), USER("用户"), GROUP("群组"), APPLICATION("应用"), ROLE("角色");
        Script script = groovyShell.parse(scriptContent); //将该方法提出去，以免重复创建，浪费堆
        return testService.groovyScript(user,script);
    }
}
