package com.gitee.freakchicken.dbapi.util;

import io.github.wujun728.sql.engine.DynamicSqlEngine;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-24 10:02
 **/
public class SqlEngineUtil {

    static DynamicSqlEngine engine = new DynamicSqlEngine();

    public static DynamicSqlEngine getEngine() {
        return engine;
    }
}
