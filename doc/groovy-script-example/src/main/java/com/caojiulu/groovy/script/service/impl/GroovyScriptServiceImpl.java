/*
 * Copyright (c) 2016 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */
package com.caojiulu.groovy.script.service.impl;

import com.caojiulu.groovy.script.service.GroovyScriptService;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2020/5/6
 * groovy 脚本服务类
 * @author Jiulu Cao
 */
@Service
public class GroovyScriptServiceImpl implements GroovyScriptService {

    @Autowired
    private Binding groovyBinding;

    /**
     *
     * @param object 实体对象类 如RGANIZATION("部门"), USER("用户"), GROUP("群组"), APPLICATION("应用"), ROLE("角色");
     * @param script 脚本的内容，存在数据库里，或者映射模块那里
     * @return
     */
    @Override
    public Object groovyScript(Object object,Script script) {
        groovyBinding.setProperty("entity", object);
        return script.run();
    }
}