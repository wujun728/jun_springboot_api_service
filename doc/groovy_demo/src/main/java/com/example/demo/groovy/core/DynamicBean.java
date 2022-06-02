/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.example.demo.groovy.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 需要动态加载到Spring中的Bean配置信息
 */
public final class DynamicBean {

    /**
     * 存储属性的map
     */
    private final Map<String, String> properties = new HashMap<String, String>();

    /**
     * 添加属性
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        properties.put(key, value);
    }

    /**
     * 遍历属性
     *
     * @return
     */
    public Iterator<String> keyIterator() {
        return properties.keySet().iterator();
    }

    /**
     * 返回属性值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return properties.get(key);
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        StringBuilder retValue = new StringBuilder("DynamicBean[");
        retValue.append("properties=").append(this.properties).append(']');
        return retValue.toString();
    }

}
