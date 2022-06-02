/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.example.demo.groovy.core;

import org.springframework.util.Assert;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * CE专用Groovy兼容处理类
 */
public class DelegatedEntityResolver implements EntityResolver {

    private final Map<EntityResolver, String> resolvers = new LinkedHashMap<EntityResolver, String>(
            2);

    /**
     * CE兼容性处理
     *
     * @param resolver
     * @param resolverToString
     */
    public void addEntityResolver(EntityResolver resolver, String resolverToString) {
        Assert.notNull(resolver);
        resolvers.put(resolver, resolverToString);
    }

    /**
     * @see EntityResolver#resolveEntity(String, String)
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
            IOException {

        for (Iterator<Entry<EntityResolver, String>> iterator = resolvers.entrySet().iterator(); iterator
                .hasNext(); ) {
            Entry<EntityResolver, String> entry = iterator.next();
            EntityResolver entityResolver = entry.getKey();

            InputSource entity = entityResolver.resolveEntity(publicId, systemId);

            if (entity != null) {
                return entity;
            }
        }
        return null;
    }
}
