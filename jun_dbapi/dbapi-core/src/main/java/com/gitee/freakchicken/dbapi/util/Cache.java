package com.gitee.freakchicken.dbapi.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static volatile Map<String, String> status = new ConcurrentHashMap<>();
}
