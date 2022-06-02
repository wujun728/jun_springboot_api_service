/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.example.demo.groovy.core;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * CE专用Groovy兼容处理类
 */
public class DelegatedNamespaceHandlerResolver implements NamespaceHandlerResolver {

    private final Map<NamespaceHandlerResolver, String> resolvers = new LinkedHashMap<NamespaceHandlerResolver, String>(
            2);

    /**
     * CE兼容性处理
     *
     * @param resolver
     * @param resolverToString
     */
    public void addNamespaceHandler(NamespaceHandlerResolver resolver, String resolverToString) {
        Assert.notNull(resolver);
        resolvers.put(resolver, resolverToString);
    }

    /**
     * @see org.springframework.beans.factory.xml.NamespaceHandlerResolver#resolve(String)
     */
    @Override
    public NamespaceHandler resolve(String namespaceUri) {

        for (Iterator<Entry<NamespaceHandlerResolver, String>> iterator = resolvers.entrySet()
                .iterator(); iterator.hasNext(); ) {
            Entry<NamespaceHandlerResolver, String> entry = iterator.next();
            NamespaceHandlerResolver handlerResolver = entry.getKey();
            NamespaceHandler handler = handlerResolver.resolve(namespaceUri);

            if (handler != null) {
                return handler;
            }

        }
        return null;
    }
}