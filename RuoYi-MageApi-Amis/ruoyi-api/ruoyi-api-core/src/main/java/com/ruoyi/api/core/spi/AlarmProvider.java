package com.ruoyi.api.core.spi;

public interface AlarmProvider {

    String getName();

    void send(String title, String message, Throwable throwable);

}