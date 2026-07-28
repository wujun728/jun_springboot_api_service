package com.ruoyi.api.core.spi;

public interface AuthProvider {

    boolean authenticate(String token, String path, String method);

    String getType();

}