package com.yuyaogc.lowcode.engine.cglib;

import java.lang.reflect.Method;
import java.util.Map;

public interface Invocation {
    <T> T invoke2(Method method, Map<String, Object> inArgsValues);

    <T> T invoke(Method method, Object... args) ;
}
