package com.caojiulu.groovy.script.config;

import com.caojiulu.groovy.script.component.GroovyScript;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
/**
 * 2020/5/6
 *  注入GroovyShell 以及 Binding类
 * @author Jiulu Cao
 */
@Configuration
public class GroovyBindingConfig {

    @Bean
    public GroovyShell groovyShell(){
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(this.getClass().getClassLoader());
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding("utf-8");
        compilerConfiguration.setScriptBaseClass(GroovyScript.class.getName());
        GroovyShell groovyShell = new GroovyShell(groovyClassLoader, groovyBinding(), compilerConfiguration);
        return groovyShell;
    }

    @Bean("groovyBinding")
    public Binding groovyBinding() {
        return new Binding();
    }
}
