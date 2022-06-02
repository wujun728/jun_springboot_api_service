/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.example.demo.groovy.core;

import com.example.demo.groovy.cache.GroovyInfo;
import com.example.demo.groovy.cache.GroovyInnerCache;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p>
 * Groovy脚本数据库数据源，实现了spring的ScriptSource接口供spring来管理Groovy脚本<br>
 * 每次获取解析报文的Groovy脚本时，从已有的缓存 ParserCache 中读取。
 */
public final class DatabaseScriptSource implements ScriptSource {

    /**
     * 脚本名称
     */
    private String scriptName;

    /**
     * 构造函数
     *
     * @param scriptName
     */
    public DatabaseScriptSource(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
     * @see org.springframework.scripting.ScriptSource#getScriptAsString()
     */
    public String getScriptAsString() throws IOException {
        // 从内部缓存获取
        GroovyInfo groovyInfo = GroovyInnerCache.getByName(scriptName);
        if (groovyInfo != null) {
            return groovyInfo.getGroovyContent();
        } else {
            return "";
        }
    }

    /**
     * @see org.springframework.scripting.ScriptSource#isModified()
     */
    public boolean isModified() {
        return false;
    }

    /**
     * @see org.springframework.scripting.ScriptSource#suggestedClassName()
     */
    public String suggestedClassName() {
        return StringUtils.stripFilenameExtension(this.scriptName);
    }

}
