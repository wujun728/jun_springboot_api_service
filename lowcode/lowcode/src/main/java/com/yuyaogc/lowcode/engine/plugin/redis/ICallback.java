package com.yuyaogc.lowcode.engine.plugin.redis;

/**
 * @author mjy
 * @param <T>
 */
@FunctionalInterface
public interface ICallback<T> {
	T call(Cache cache);
}
