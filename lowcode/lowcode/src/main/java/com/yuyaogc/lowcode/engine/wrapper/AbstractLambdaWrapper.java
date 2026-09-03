package com.yuyaogc.lowcode.engine.wrapper;

import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.func.SFunction;
import com.yuyaogc.lowcode.engine.lambda.LambdaMeta;
import com.yuyaogc.lowcode.engine.lambda.ReflectLambdaMeta;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
public abstract class AbstractLambdaWrapper<T, Children extends AbstractLambdaWrapper<T, Children>>
        extends Criteria<T, SFunction<T, ?>, Children> {


    @Override
    @SafeVarargs
    public final Children groupBy(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.groupBy(condition, column, columns);
    }


    @Override
    @SafeVarargs
    public final Children orderByAsc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(condition, column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(condition, column, columns);
    }

    @Override
    protected String columnToString(SFunction<T, ?> column) {
        LambdaMeta meta = extract(column);
        String fieldName = methodToProperty(meta.getImplMethodName());
        Class<?> instantiatedClass = meta.getInstantiatedClass();
        return fieldName;
    }

    public String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new EngineException(
                    "Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    public <T> LambdaMeta extract(SFunction<T, ?> func) {
        // 2. 反射读取
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return new ReflectLambdaMeta((SerializedLambda) method.invoke(func), func.getClass().getClassLoader());
        } catch (Throwable e) {
            // 3. 反射失败使用序列化的方式读取
            throw new EngineException(e);
        }
    }


}
