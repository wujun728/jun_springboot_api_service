package org.harmony.toddler.mybatis.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.harmony.toddler.mybatis.util.PluginUtil;
import org.harmony.toddler.mybatis.util.SqlParserUtil;

import java.sql.Connection;
import java.util.Properties;

/**
 * 数据权限拦截器
 */
@Intercepts(value = {@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class})})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = PluginUtil.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");

        // 待执行的sql，在这里也就是预编译后的sql，即参数位都是?号
        String sql = boundSql.getSql();
        System.out.println("Plugin: " + sql);
        sql = SqlParserUtil.toCamelAlias(sql);
        if (StringUtils.isNotEmpty(sql)) {
            metaObject.setValue("delegate.boundSql.sql", sql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}