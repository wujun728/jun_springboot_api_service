package com.yuyaogc.lowcode.engine.plugin.activerecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * SqlReporter.
 */
public class SqlReporter implements InvocationHandler {
    static Logger log = LoggerFactory.getLogger(SqlReporter.class);
    private Connection conn;
    private static boolean logOn = false;

    public SqlReporter(Connection conn) {
        this.conn = conn;
    }

    public static void setLog(boolean on) {
        SqlReporter.logOn = on;
    }

    @SuppressWarnings("rawtypes")
    public Connection getConnection() {
        Class clazz = conn.getClass();
        return (Connection) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{Connection.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equals("prepareStatement")) {
                String info = "Sql: " + args[0];
                if (logOn)
                    log.info(info);
                else
                    System.out.println(info);
            }
            return method.invoke(conn, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
