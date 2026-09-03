package com.gitee.sqlrest.core.exec.engine.impl;

import cn.hutool.core.util.NumberUtil;
import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.core.exec.engine.AbstractExecutorEngine;
import com.gitee.sqlrest.core.util.SqlJdbcUtils;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import com.gitee.sqlrest.template.Configuration;
import com.gitee.sqlrest.template.SqlMeta;
import com.gitee.sqlrest.template.SqlTemplate;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlExecutorService extends AbstractExecutorEngine {

  public SqlExecutorService(HikariDataSource dataSource, ProductTypeEnum productType) {
    super(dataSource, productType);
  }

  @Override
  public Object execute(List<ApiContextEntity> scripts, Map<String, Object> params, NamingStrategyEnum strategy) {
    List<Object> dataList = new ArrayList<>();
    Configuration cfg = new Configuration();
    try (Connection connection = this.dataSource.getConnection()) {
      try {
        connection.setAutoCommit(false);
        for (ApiContextEntity sql : scripts) {
          SqlTemplate template = cfg.getTemplate(sql.getSqlText());
          SqlMeta sqlMeta = template.process(params);
          int page = (null == params.get(Constants.PARAM_PAGE_NUMBER))
              ? 1
              : NumberUtil.parseInt(params.get(Constants.PARAM_PAGE_NUMBER).toString());
          int size = (null == params.get(Constants.PARAM_PAGE_SIZE))
              ? 10
              : NumberUtil.parseInt(params.get(Constants.PARAM_PAGE_SIZE).toString());
          dataList.add(SqlJdbcUtils.execute(productType, connection, sqlMeta, strategy, page, size));
        }
        connection.commit();
        return dataList;
      } catch (Exception e) {
        try {
          connection.rollback();
        } catch (SQLException se) {
          log.warn("Failed to call jdbc Connection::rollback(): {}", se.getMessage(), se);
        }
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
