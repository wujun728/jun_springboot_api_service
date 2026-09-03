package com.gitee.sqlrest.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.gitee.sqlrest.common.dto.PageResult;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.JdbcUrlUtils;
import com.gitee.sqlrest.core.driver.DriverLoadService;
import com.gitee.sqlrest.core.dto.DataSourceSaveRequest;
import com.gitee.sqlrest.core.dto.DatabaseTypeDetailResponse;
import com.gitee.sqlrest.core.dto.DatasourceDetailResponse;
import com.gitee.sqlrest.core.dto.EntityIdNameResponse;
import com.gitee.sqlrest.core.dto.EntitySearchRequest;
import com.gitee.sqlrest.core.dto.MetadataColumnResponse;
import com.gitee.sqlrest.core.util.DataSourceUtils;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.dao.DataSourceDao;
import com.gitee.sqlrest.persistence.entity.DataSourceEntity;
import com.gitee.sqlrest.persistence.util.PageUtils;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {

  @Resource
  private DataSourceDao dataSourceDao;

  public List<DatabaseTypeDetailResponse> getTypes() {
    List<DatabaseTypeDetailResponse> lists = new ArrayList<>();
    for (ProductTypeEnum type : ProductTypeEnum.values()) {
      DatabaseTypeDetailResponse detail = new DatabaseTypeDetailResponse();
      detail.setId(type.getId());
      detail.setType(type.getName().toUpperCase());
      detail.setDriver(type.getDriver());
      detail.setSample(type.getSample());
      lists.add(detail);
    }

    return lists;
  }

  public PageResult<DatasourceDetailResponse> searchList(EntitySearchRequest request) {
    Supplier<List<DatasourceDetailResponse>> method = () -> {
      List<DataSourceEntity> list = dataSourceDao.listAll(request.getSearchText());
      return list.stream().map(dataSourceEntity -> {
        DatasourceDetailResponse response = new DatasourceDetailResponse();
        BeanUtil.copyProperties(dataSourceEntity, response);
        return response;
      }).collect(Collectors.toList());
    };

    return PageUtils.getPage(method, request.getPage(), request.getSize());
  }

  public DatasourceDetailResponse getDetailById(Long id) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    DatasourceDetailResponse response = new DatasourceDetailResponse();
    BeanUtil.copyProperties(dataSourceEntity, response);
    return response;
  }

  public void testDataSource(Long id) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    File driverPathFile = SpringUtil.getBean(DriverLoadService.class)
        .getVersionDriverFile(dataSourceEntity.getType(),
            dataSourceEntity.getVersion());
    String driverPath = driverPathFile.getAbsolutePath();
    HikariDataSource ds = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath);
    if (StringUtils.isNotBlank(dataSourceEntity.getType().getSql())) {
      try (Connection connection = ds.getConnection()) {
        Statement statement = connection.createStatement();
        statement.execute(dataSourceEntity.getType().getSql());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void createDataSource(DataSourceSaveRequest request) {
    if (StringUtils.isBlank(request.getName())) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "name is empty");
    }
    if (Objects.nonNull(dataSourceDao.getByName(request.getName()))) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS,
          "name [" + request.getName() + "] already exists");
    }

    DataSourceEntity dataSourceEntity = new DataSourceEntity();
    BeanUtil.copyProperties(request, dataSourceEntity);

    validJdbcUrlFormat(dataSourceEntity);
    dataSourceDao.insert(dataSourceEntity);
  }

  public void updateDataSource(DataSourceSaveRequest request) {
    if (Objects.isNull(request.getId()) || Objects.isNull(dataSourceDao.getById(request.getId()))) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + request.getId());
    }

    DataSourceEntity exist = dataSourceDao.getByName(request.getName());
    if (Objects.nonNull(exist) && !exist.getId().equals(request.getId())) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, "name=" + request.getName());
    }

    DataSourceEntity dataSourceEntity = new DataSourceEntity();
    BeanUtil.copyProperties(request, dataSourceEntity);

    validJdbcUrlFormat(dataSourceEntity);
    dataSourceDao.updateById(dataSourceEntity);
  }

  public void deleteDataSource(Long id) {
    if (SpringUtil.getBean(ApiAssignmentDao.class).existsDataSourceById(id)) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_USED, "id=" + id);
    }
    dataSourceDao.deleteById(id);
    DataSourceUtils.dropHikariDataSource(id);
  }

  public PageResult<EntityIdNameResponse> getDataSourceNames(Integer page, Integer size) {
    Supplier<List<EntityIdNameResponse>> method = () -> {
      List<DataSourceEntity> lists = dataSourceDao.listAll(null);
      return lists.stream()
          .map(c -> new EntityIdNameResponse(c.getId(), c.getName()))
          .collect(Collectors.toList());
    };

    return PageUtils.getPage(method, page, size);
  }

  public List<String> getDatasourceSchemas(Long id) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    File driverPathFile = SpringUtil.getBean(DriverLoadService.class)
        .getVersionDriverFile(dataSourceEntity.getType(),
            dataSourceEntity.getVersion());
    String driverPath = driverPathFile.getAbsolutePath();
    String sqlList = dataSourceEntity.getType().getContext().getSqlSchemaList();
    if (null == sqlList) {
      return dataSourceEntity.getType().getContext().getRetSchemaList();
    }
    List<String> result = new ArrayList<>();
    HikariDataSource ds = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath);
    if (StringUtils.isNotBlank(sqlList)) {
      try (Connection connection = ds.getConnection();
          Statement statement = connection.createStatement();
          ResultSet rs = statement.executeQuery(sqlList)) {
        while (rs.next()) {
          result.add(rs.getString(1));
        }
        return result;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  public List<String> getSchemaTables(Long id, String schema) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    File driverPathFile = SpringUtil.getBean(DriverLoadService.class)
        .getVersionDriverFile(dataSourceEntity.getType(),
            dataSourceEntity.getVersion());
    String driverPath = driverPathFile.getAbsolutePath();
    ProductTypeEnum productType = dataSourceEntity.getType();
    List<String> result = new ArrayList<>();
    HikariDataSource ds = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath);
    try (Connection connection = ds.getConnection()) {
      String catalogName = productType.getContext().isHasCatalogAndSchema()
          ? connection.getCatalog()
          : productType.getContext().getAdapter().apply(schema).getLeft();
      String schemaName = productType.getContext().getAdapter().apply(schema).getRight();
      try (ResultSet rs = connection.getMetaData()
          .getTables(catalogName, schemaName, "%", new String[]{"TABLE"})) {
        while (rs.next()) {
          result.add(rs.getString("TABLE_NAME"));
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public List<String> getSchemaViews(Long id, String schema) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    File driverPathFile = SpringUtil.getBean(DriverLoadService.class)
        .getVersionDriverFile(dataSourceEntity.getType(),
            dataSourceEntity.getVersion());
    String driverPath = driverPathFile.getAbsolutePath();
    ProductTypeEnum productType = dataSourceEntity.getType();
    if (StringUtils.isBlank(productType.getContext().getSqlSchemaList())) {
      return productType.getContext().getRetSchemaList();
    }
    List<String> result = new ArrayList<>();
    HikariDataSource ds = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath);
    try (Connection connection = ds.getConnection()) {
      String catalogName = productType.getContext().isHasCatalogAndSchema()
          ? connection.getCatalog()
          : productType.getContext().getAdapter().apply(schema).getLeft();
      String schemaName = productType.getContext().getAdapter().apply(schema).getRight();
      try (ResultSet rs = connection.getMetaData()
          .getTables(catalogName, schemaName, "%", new String[]{"VIEW"})) {
        while (rs.next()) {
          result.add(rs.getString("TABLE_NAME"));
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public List<MetadataColumnResponse> getTableColumns(Long id, String schema, String table) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(id);
    File driverPathFile = SpringUtil.getBean(DriverLoadService.class)
        .getVersionDriverFile(dataSourceEntity.getType(),
            dataSourceEntity.getVersion());
    String driverPath = driverPathFile.getAbsolutePath();
    ProductTypeEnum productType = dataSourceEntity.getType();
    List<MetadataColumnResponse> result = new ArrayList<>();
    HikariDataSource ds = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath);
    try (Connection connection = ds.getConnection()) {
      String catalogName = productType.getContext().isHasCatalogAndSchema()
          ? connection.getCatalog()
          : productType.getContext().getAdapter().apply(schema).getLeft();
      String schemaName = productType.getContext().getAdapter().apply(schema).getRight();
      try (ResultSet rs = connection.getMetaData()
          .getColumns(catalogName, schemaName, table, null)) {
        while (rs.next()) {
          String columnName = rs.getString("COLUMN_NAME");
          String typeName = rs.getString("TYPE_NAME");
          String remarks = rs.getString("REMARKS");
          result.add(MetadataColumnResponse.builder()
              .name(columnName).type(typeName).remarks(remarks)
              .build());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  private void validJdbcUrlFormat(DataSourceEntity conn) {
    String typeName = conn.getType().getName().toUpperCase();
    ProductTypeEnum supportDbType = ProductTypeEnum.valueOf(typeName);
    if (!conn.getUrl().startsWith(supportDbType.getUrlPrefix())) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_JDBC_URL, conn.getUrl());
    }

    for (int i = 0; i < supportDbType.getUrl().length; ++i) {
      String pattern = supportDbType.getUrl()[i];
      Matcher matcher = JdbcUrlUtils.getPattern(pattern).matcher(conn.getUrl());
      if (!matcher.matches()) {
        if (i == supportDbType.getUrl().length - 1) {
          throw new CommonException(ResponseErrorCode.ERROR_INVALID_JDBC_URL, conn.getUrl());
        }
      } else {
        if (supportDbType.hasDatabaseName() && StringUtils.isBlank(matcher.group("database"))) {
          throw new CommonException(ResponseErrorCode.ERROR_INVALID_JDBC_URL,
              "库名没有指定 :" + conn.getUrl());
        }
        if (supportDbType.hasFilePath() && StringUtils.isBlank(matcher.group("file"))) {
          throw new CommonException(ResponseErrorCode.ERROR_INVALID_JDBC_URL,
              "文件路径没有指定 :" + conn.getUrl());
        }

        break;
      }
    }
  }
}
