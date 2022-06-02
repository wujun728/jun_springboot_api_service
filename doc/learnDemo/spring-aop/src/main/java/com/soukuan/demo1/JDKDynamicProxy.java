package com.soukuan.demo1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * 基于接口进行动态代理
 *
 * Proxy类的newInstance()方法有三个参数：
 * 　　　　ClassLoader loader：它是类加载器类型，你不用去理睬它，你只需要知道怎么可以获得它就可以了：MyInterface.class.getClassLoader()就可以获取到ClassLoader对象，没错，
 * 只要你有一个Class对象就可以获取到ClassLoader对象；
 * 　　　　Class[] interfaces：指定newProxyInstance()方法返回的对象要实现哪些接口，没错，可以指定多个接口，例如上面例子只我们只指定了一个接口：Class[] cs = {MyInterface.class};
 * 　　　　InvocationHandler h：它是最重要的一个参数！它是一个接口！它的名字叫调用处理器！你想一想，上面例子中mi对象是MyInterface接口的实现类对象，那么它一定是可以调用fun1()和fun2()方法了，
 * 难道你不想调用一下fun1()和fun2()方法么，它会执行些什么东东呢？其实无论你调用代理对象的什么方法，它都是在调用InvocationHandler的invoke()方法！
 * InvocationHandler的invoke()方法的参数有三个：
 * 　　　　Object proxy：代理对象，也就是Proxy.newProxyInstance()方法返回的对象，通常我们用不上它；
 * 　　　　Method method：表示当前被调用方法的反射对象，例如mi.fun1()，那么method就是fun1()方法的反射对象；
 * 　　　　Object[] args：表示当前被调用方法的参数，当然mi.fun1()这个调用是没有参数的，所以args是一个零长数组。
 */
public class JDKDynamicProxy {

    public static Object createProxy(final Object target){
        return Proxy.newProxyInstance(Say.class.getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.getName().equals("sayHello")){
                    doBefore();
                    try {
                        method.invoke(target, args);
                    } catch (Exception e) {
                        doThrowing();
                    }
                    doAfter();
                }
                return null;
            }
        });
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
