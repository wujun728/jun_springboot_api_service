package com.yuyaogc.lowcode.engine.func;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author miemie
 * @since 2018-12-12
 */
public interface Query<Children, T, R> extends Serializable {

    /**
     * 指定查询字段
     *
     * @param columns 字段列表
     * @return children
     */
    @SuppressWarnings("unchecked")
    default Children select(R... columns) {
        return select(true, columns);
    }

    /**
     * 指定查询字段
     *
     * @param condition 执行条件
     * @param columns   字段列表
     * @return children
     */
    @SuppressWarnings("unchecked")
    default Children select(boolean condition, R... columns) {
        return select(condition, Arrays.asList(columns));
    }

    /**
     * 指定查询字段
     *
     * @param columns 字段列表
     * @return children
     */
    default Children select(List<R> columns) {
        return select(true, columns);
    }

    /**
     * 指定查询字段
     *
     * @param condition 执行条件
     * @param columns   字段列表
     * @return children
     */
    Children select(boolean condition, List<R> columns);

    /**
     * 查询条件 SQL 片段
     */
    String getSqlSelect();
}