/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.example.demo.groovy.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class BeanNameCache {

    /**
     * 脚本列表
     */
    private static ConcurrentMap<String, String> beanNameMap = new ConcurrentHashMap<>();

    /**
     * 把脚本缓存一下
     *
     * @param beanNameList
     */
    public static void put2map(List<BeanName> beanNameList) {
        // 先清空
        if (!beanNameMap.isEmpty()) {
            beanNameMap.clear();
        }
        for (BeanName beanName : beanNameList) {
            if (!beanNameMap.containsKey(beanName.getInterfaceId())) {
                beanNameMap.put(beanName.getInterfaceId(), beanName.getBeanName());
            } else {
                log.warn("found duplication interfaceId:" + beanName.getInterfaceId());
            }
        }
    }

    public static String getByInterfaceId(String interfaceId) {
        return beanNameMap.get(interfaceId);
    }
}
