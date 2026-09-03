package com.gitee.sqlrest.core.exec.engine;

import com.gitee.sqlrest.common.enums.ExecuteEngineEnum;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.core.exec.engine.impl.ScriptExecutorService;
import com.gitee.sqlrest.core.exec.engine.impl.SqlExecutorService;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ApiExecutorEngineFactory {

  private static Map<ExecuteEngineEnum, BiFunction<HikariDataSource, ProductTypeEnum, ApiExecutorEngine>> engineMap = new HashMap<>();

  static {
    engineMap.put(ExecuteEngineEnum.SQL, SqlExecutorService::new);
    engineMap.put(ExecuteEngineEnum.SCRIPT, ScriptExecutorService::new);
  }

  public static ApiExecutorEngine getExecutor(ExecuteEngineEnum engine, HikariDataSource dataSource,
      ProductTypeEnum productType) {
    BiFunction<HikariDataSource, ProductTypeEnum, ApiExecutorEngine> creator = engineMap.get(engine);
    if (null == creator) {
      throw new RuntimeException("Unsupported engine :" + engine);
    }
    return creator.apply(dataSource, productType);
  }

}
