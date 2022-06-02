package com.caojiulu.groovy.script.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.springframework.stereotype.Service;


public interface GroovyScriptService {

     Object groovyScript(Object object,Script script);
}
