package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.util.*;

public class KvMap implements Map<String, Object> {
    Map<String, Object> data;
    Set<String> modifyFlag;
    static KvMap empty;

    static {
        empty = new KvMap(0);
        empty.data = Collections.emptyMap();
    }

    public static KvMap empty() {
        return empty;
    }

    public Map<String, Object> getData() {
        return data;
    }

    /**
     *
     */
    public KvMap() {
        data = new HashMap<>();
    }

    public KvMap(int initialCapacity) {
        data = new HashMap<>(initialCapacity);
    }

    public KvMap(Map<? extends String, ? extends Object> m) {
        data = new HashMap<>(m);
    }

    public KvMap set(String key, Object value) {
        put(key, value);
        _getModifyFlag().add(key);
        return this;
    }

    public Set<String> _getModifyFlag() {
        if (modifyFlag == null) {
            modifyFlag = new HashSet<>();
        }
        return modifyFlag;
    }

    public void clearModifyFlag() {
        if (modifyFlag != null) {
            modifyFlag.clear();
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return data.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        data.putAll(m);
    }

    @Override
    public void clear() {
        data.clear();
        clearModifyFlag();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return data.entrySet();
    }


}
