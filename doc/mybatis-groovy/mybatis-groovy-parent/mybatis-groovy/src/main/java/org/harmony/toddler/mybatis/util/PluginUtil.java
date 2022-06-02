package org.harmony.toddler.mybatis.util;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Proxy;

public class PluginUtil {
    public static final String DELEGATE_BOUND_SQL = "delegate.boundSql.sql";

    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        } else {
            return (T) target;
        }
    }

}
