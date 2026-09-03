package com.yuyaogc.lowcode.engine.cglib;

import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.exception.EngineLogger;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.util.JsonUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static com.yuyaogc.lowcode.engine.util.BeanUtils.getTypeClass;

public class CglibInvocation implements Invocation{
    private static EngineLogger log = EngineLogger.me(CglibInvocation.class);

    public <T> T invoke2(Method method,Map<String, Object> inArgsValues) {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Parameter parameter = params[i];
            String argName = parameter.getName();
            Object arg = inArgsValues.get(argName);
            args[i] = arg;
            if(Objects.isNull(arg)){
                continue;
            }
            if (!parameter.getType().isAssignableFrom(arg.getClass())) {
                try {
                    String s = JsonUtil.objectToString(arg);
                    Object o = JsonUtil.stringToObject(s, parameter.getType());
                    args[i] = o;
                } catch (Exception e) {

                }
            } else if (arg instanceof Iterable) {
                Iterable<?> objects = (Iterable<?>) arg;
                // 参数集合中第一个非null对象的类型
                Class<?> argInnerType = null;
                for (Object object : objects) {
                    if (object != null) {
                        argInnerType = object.getClass();
                        break;
                    }
                }
                if (argInnerType == null) {
                    continue;
                }

                Class<?> parameterClazz = getTypeClass(parameter);
                if (!Objects.isNull(parameterClazz) && !parameterClazz.isAssignableFrom(argInnerType)) {
                    try {
                        String json = JsonUtil.objectToString(arg);
                        args[i] = JsonUtil.stringToList(json, parameterClazz);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return invoke(method,args);
    }

    public <T> T invoke(Method method,Object... args) {
        Connection conn = Db.getConfig().getThreadLocalConnection();
        if (conn != null) {
            try {
                return (T) method.invoke(Proxy.getObject(method), args);
            } catch (Exception e) {
                throw new EngineException(e);
            }
        }

        try {
            conn = Db.getConfig().getConnection();
            Db.getConfig().setThreadLocalConnection(conn);
            try {
                return (T) method.invoke(Proxy.getObject(method), args);
            } catch (Exception e) {
                log.error("method invoke exception",e);
                throw new EngineException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Db.getConfig().removeThreadLocalConnection();
            if (conn != null) {
                try{conn.close();}catch(Exception e){System.err.println(e.getMessage());}
            }
        }
    }
}
