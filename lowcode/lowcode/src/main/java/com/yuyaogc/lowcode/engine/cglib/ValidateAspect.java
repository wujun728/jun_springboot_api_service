package com.yuyaogc.lowcode.engine.cglib;



import java.lang.reflect.Method;


public class ValidateAspect implements Aspect {
    private static final long serialVersionUID = 1L;



    @Override
    public boolean before(Object target, Method method, Object[] args) {
        switch (method.getName()) {
            case "create":
            case "update":
                break;
            default: {
            }
        }
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        return true;
    }
}
