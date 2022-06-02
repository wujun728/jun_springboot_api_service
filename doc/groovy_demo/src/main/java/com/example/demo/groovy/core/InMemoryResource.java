/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.example.demo.groovy.core;

import org.springframework.core.io.AbstractResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 内存资源，主要用来将编译后的解析脚本存于内存
 */
public class InMemoryResource extends AbstractResource {

    /**
     * 资源描述
     */
    private static final String DESCRIPTION = "InMemoryResource";

    /**
     * 脚本来源
     */
    private final byte[] source;

    /**
     * @param sourceString
     */
    public InMemoryResource(String sourceString) {
        this.source = sourceString.getBytes();
    }

    /**
     * @see org.springframework.core.io.Resource#getDescription()
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * @see org.springframework.core.io.InputStreamSource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(source);
    }

    /**
     * @see org.springframework.core.io.AbstractResource#hashCode()
     */
    public int hashCode() {
        return Arrays.hashCode(source);
    }

    /**
     * @see org.springframework.core.io.AbstractResource#equals(Object)
     */
    public boolean equals(Object res) {
        if (!(res instanceof InMemoryResource)) {
            return false;
        }

        return Arrays.equals(source, ((InMemoryResource) res).source);
    }

}
