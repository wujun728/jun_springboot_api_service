package com.soukuan.demo2;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * 基于子类进行动态代理
 */
public class CglibProxy {

    public static <T> T createProxy(final T t){
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(CglibProxy.class.getClassLoader());
        enhancer.setSuperclass(t.getClass());
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object ret=null;
                if(method.getName().equals("sayHello")){
                    doBefore();
                    try {
                        ret=method.invoke(t, args);
                    } catch (Exception e) {
                        doThrowing();
                    }
                    doAfter();
                }
                return ret;
            }
        });
        return (T)enhancer.create();
    }

    private static void doThrowing() {
        System.out.println("AOP say throw a exception");
    }

    private static void doBefore() {
        System.out.println("AOP before say");
    }

    private static void doAfter() {
        System.out.println("AOP after say");
    }
}
