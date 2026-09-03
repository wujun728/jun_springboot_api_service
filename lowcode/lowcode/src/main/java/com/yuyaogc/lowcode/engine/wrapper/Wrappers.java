package com.yuyaogc.lowcode.engine.wrapper;

/**
 * Wrapper 条件构造
 *
 * @author Caratacus
 */
public final class Wrappers {


    private Wrappers() {
        // ignore
    }


    /**
     * 获取 LambdaQueryWrapper&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return LambdaQueryWrapper&lt;T&gt;
     */
    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

}
