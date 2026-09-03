package com.yuyaogc.lowcode.engine.plugin.redis;

/**
 * @author mjy
 */
public interface ISerializer {

    byte[] keyToBytes(String key);
    String keyFromBytes(byte[] bytes);

    byte[] fieldToBytes(Object field);
    Object fieldFromBytes(byte[] bytes);

	byte[] valueToBytes(Object value);
    Object valueFromBytes(byte[] bytes);
}
