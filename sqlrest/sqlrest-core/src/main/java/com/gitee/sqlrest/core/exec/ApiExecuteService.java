package com.gitee.sqlrest.core.exec;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitee.sqlrest.common.dto.ItemParam;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.ParamLocationEnum;
import com.gitee.sqlrest.common.enums.ParamTypeEnum;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.driver.DriverLoadService;
import com.gitee.sqlrest.core.exec.engine.ApiExecutorEngineFactory;
import com.gitee.sqlrest.core.util.DataSourceUtils;
import com.gitee.sqlrest.persistence.dao.DataSourceDao;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.gitee.sqlrest.persistence.entity.DataSourceEntity;
import com.google.common.base.Charsets;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiExecuteService {

  @Resource
  private DataSourceDao dataSourceDao;
  @Resource
  private DriverLoadService driverLoadService;

  public ResultEntity<Object> execute(ApiAssignmentEntity config, HttpServletRequest request) {
    try {
      DataSourceEntity dsEntity = dataSourceDao.getById(config.getDatasourceId());
      if (null == dsEntity) {
        String message = "datasource[id=" + config.getDatasourceId() + " not exist!";
        log.warn("Error for handle api[id={}],information:{}", config.getId(), message);
        return ResultEntity.failed(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, message);
      }
      Map<String, Object> paramValues = obtainParameterValues(request, config.getParams());
      File driverPath = driverLoadService.getVersionDriverFile(dsEntity.getType(), dsEntity.getVersion());
      HikariDataSource dataSource = DataSourceUtils.getHikariDataSource(dsEntity, driverPath.getAbsolutePath());
      Object result = ApiExecutorEngineFactory
          .getExecutor(config.getEngine(), dataSource, dsEntity.getType())
          .execute(config.getContextList(), paramValues, config.getNamingStrategy());
      if (result instanceof Collection) {
        Collection r = (Collection) result;
        return ResultEntity.success(config.getContextList().size() == 1 ? r.stream().findFirst().get() : r);
      }
      return ResultEntity.success(result);
    } catch (Throwable t) {
      return ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR, ExceptionUtil.getMessage(t));
    }
  }

  private Map<String, Object> obtainParameterValues(HttpServletRequest request, List<ItemParam> params) {
    Map<String, Object> map = new HashMap<>();
    if (null != params && params.size() > 0) {
      Map<String, Object> bodyMap = getRequestBodyMap(request);
      for (ItemParam param : params) {
        String name = param.getName();
        ParamTypeEnum type = param.getType();
        ParamLocationEnum location = param.getLocation();
        String defaultValue = param.getDefaultValue();
        if (location == ParamLocationEnum.REQUEST_HEADER) {
          map.put(name, request.getHeader(name));
        } else if (location == ParamLocationEnum.REQUEST_BODY) {
          map.put(name, bodyMap.get(name));
        } else {
          boolean isArray = Optional.ofNullable(param.getIsArray()).orElse(false);
          Boolean required = Optional.ofNullable(param.getRequired()).orElse(false);
          if (isArray) {
            String[] values = request.getParameterValues(name);
            if (null != values && values.length > 0) {
              List list = Arrays.asList(values).stream()
                  .map(v -> type.getConverter().apply(v))
                  .collect(Collectors.toList());
              map.put(name, list);
            } else {
              map.put(name, null);
            }
          } else {
            String value = request.getParameter(name);
            if (!required && null == value) {
              value = defaultValue;
            }
            map.put(name, type.getConverter().apply(value));
          }
        }
      }
    }
    return map;
  }

  public Map<String, Object> getRequestBodyMap(HttpServletRequest request) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String jsonString = IoUtil.read(request.getInputStream(), Charsets.UTF_8);
      if (StringUtils.isBlank(jsonString)) {
        return Collections.emptyMap();
      }
      Map<String, Object> resultMap = new HashMap<>();
      JsonNode rootNode = mapper.readTree(jsonString);
      if (!rootNode.isContainerNode()) {
        return Collections.emptyMap();
      }

      Iterator<Entry<String, JsonNode>> fields = rootNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> entry = fields.next();
        JsonNode jsonNode = entry.getValue();
        Object value = mapper.convertValue(jsonNode, Object.class);
        resultMap.put(entry.getKey(), value);
      }
      return resultMap;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
