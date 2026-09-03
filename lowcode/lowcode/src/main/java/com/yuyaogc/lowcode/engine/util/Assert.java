package com.yuyaogc.lowcode.engine.util;


import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言类
 *
 * @author miemie
 * @since 2018-07-24
 */
public final class Assert {

    /**
     * 断言这个 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression boolean 值
     * @param message    消息
     */
    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
        }
    }

    /**
     * 断言这个 boolean 为 false
     * <p>为 true 则抛出异常</p>
     *
     * @param expression boolean 值
     * @param message    消息
     */
    public static void isFalse(boolean expression, String message, Object... params) {
        isTrue(!expression, message, params);
    }

    /**
     * 断言这个 object 为 null
     * <p>不为 null 则抛异常</p>
     *
     * @param object  对象
     * @param message 消息
     */
    public static void isNull(Object object, String message, Object... params) {
        isTrue(object == null, message, params);
    }

    /**
     * 断言这个 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object  对象
     * @param message 消息
     */
    public static void notNull(Object object, String message, Object... params) {
        isTrue(object != null, message, params);
    }

    /**
     * 断言这个 value 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param value   字符串
     * @param message 消息
     */
    public static void notEmpty(String value, String message, Object... params) {
        isTrue(StringUtils.isNotBlank(value), message, params);
    }

    /**
     * 断言这个 collection 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param collection 集合
     * @param message    消息
     */
    public static void notEmpty(Collection<?> collection, String message, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(collection), message, params);
    }

    /**
     * 断言这个 map 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map     集合
     * @param message 消息
     */
    public static void notEmpty(Map<?, ?> map, String message, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(map), message, params);
    }

    /**
     * 断言这个 map 为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map     集合
     * @param message 消息
     */
    public static void isEmpty(Map<?, ?> map, String message, Object... params) {
        isTrue(CollectionUtils.isEmpty(map), message, params);
    }

    /**
     * 断言这个 数组 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param array   数组
     * @param message 消息
     */
    public static void notEmpty(Object[] array, String message, Object... params) {
        isTrue(ArrayUtils.isNotEmpty(array), message, params);
    }
}
