package com.gitee.sqlrest.core.exec.engine;

import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.zaxxer.hikari.HikariDataSource;

public abstract class AbstractExecutorEngine implements ApiExecutorEngine {

  protected HikariDataSource dataSource;
  protected ProductTypeEnum productType;

  public AbstractExecutorEngine(HikariDataSource dataSource, ProductTypeEnum productType) {
    this.dataSource = dataSource;
    this.productType = productType;
  }
}
