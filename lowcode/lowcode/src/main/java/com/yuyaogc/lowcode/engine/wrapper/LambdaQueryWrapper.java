package com.yuyaogc.lowcode.engine.wrapper;

import com.yuyaogc.lowcode.engine.func.Query;
import com.yuyaogc.lowcode.engine.func.SFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Lambda 语法使用 Wrapper
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
public class LambdaQueryWrapper<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapper<T>>
        implements Query<LambdaQueryWrapper<T>, T, SFunction<T, ?>> {


    protected LambdaQueryWrapper<T> doSelect(boolean condition, List<SFunction<T, ?>> columns) {

        return typedThis;
    }


    @Override
    public LambdaQueryWrapper<T> select(boolean condition, List<SFunction<T, ?>> columns) {
        return doSelect(condition, columns);
    }


    @Override
    @SafeVarargs
    public final LambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        return doSelect(true, Arrays.asList(columns));
    }

    @Override
    @SafeVarargs
    public final LambdaQueryWrapper<T> select(boolean condition, SFunction<T, ?>... columns) {
        return doSelect(condition, Arrays.asList(columns));
    }

    /**
     * @since 3.5.4
     */


    @Override
    public String getSqlSelect() {
        return null;
    }


    @Override
    public void clear() {
        super.clear();
    }
}
