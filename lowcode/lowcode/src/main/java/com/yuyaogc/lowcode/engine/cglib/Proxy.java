package com.yuyaogc.lowcode.engine.cglib;

import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;

public class Proxy {
    public static <T> T getObject(Method method) {
        Enhancer e = new Enhancer();
        e.setSuperclass(method.getDeclaringClass());
        ValidateAspect aspect = new ValidateAspect();
        CglibInterceptor c = new CglibInterceptor(aspect);
        e.setCallback(c);
        return (T) e.create();
    }
}
