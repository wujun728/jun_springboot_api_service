package com.yuyaogc.lowcode.engine.cglib;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class CglibInterceptor implements MethodInterceptor, Serializable {

    private static final long serialVersionUID = 1L;

    private final Aspect aspect;


    public CglibInterceptor( Aspect aspect) {
        this.aspect = aspect;
    }



    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        if (aspect.before(method.getDeclaringClass(), method, args)) {
            try {
				result = proxy.invokeSuper(obj, args);
            } catch (InvocationTargetException e) {
                if (aspect.afterException(method.getDeclaringClass(), method, args, e.getTargetException())) {
                    throw e;
                }
            }
        }
        if (aspect.after(method.getDeclaringClass(), method, args, result)) {
            return result;
        }
        return null;
    }
}
