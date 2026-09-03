package com.yuyaogc.lowcode.engine.lambda;

import com.yuyaogc.lowcode.engine.util.ClassUtils;

import java.lang.invoke.SerializedLambda;

/**
 * Created by hcl at 2021/5/14
 */
public class ReflectLambdaMeta implements LambdaMeta {
    private final SerializedLambda lambda;

    private final ClassLoader classLoader;

    public ReflectLambdaMeta(SerializedLambda lambda, ClassLoader classLoader) {
        this.lambda = lambda;
        this.classLoader = classLoader;
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

    @Override
    public Class<?> getInstantiatedClass() {
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";")).replace("/", ".");
        return ClassUtils.toClassConfident(instantiatedType, this.classLoader);
    }

}