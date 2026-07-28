package com.ruoyi.api.openapi.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppKeyManager {

    private final Map<String, AppKeyEntry> store = new ConcurrentHashMap<>();

    public void register(String appKey, String appSecret, String name) {
        store.put(appKey, new AppKeyEntry(appKey, appSecret, name, System.currentTimeMillis()));
    }

    public boolean authenticate(String appKey, String appSecret) {
        AppKeyEntry entry = store.get(appKey);
        if (entry == null) return false;
        return entry.appSecret.equals(appSecret);
    }

    public void remove(String appKey) {
        store.remove(appKey);
    }

    public AppKeyEntry getEntry(String appKey) {
        return store.get(appKey);
    }

    public static class AppKeyEntry {
        private final String appKey;
        private final String appSecret;
        private final String name;
        private final long createdTime;

        public AppKeyEntry(String appKey, String appSecret, String name, long createdTime) {
            this.appKey = appKey; this.appSecret = appSecret;
            this.name = name; this.createdTime = createdTime;
        }

        public String getAppKey() { return appKey; }
        public String getAppSecret() { return appSecret; }
        public String getName() { return name; }
        public long getCreatedTime() { return createdTime; }
    }
}