package com.gitee.sqlrest.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.gitee.sqlrest.common.dto.PageResult;
import com.gitee.sqlrest.common.dto.ParamValue;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.DataTypeFormatEnum;
import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.common.enums.ParamTypeEnum;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.driver.DriverLoadService;
import com.gitee.sqlrest.core.dto.ApiAssignmentBaseResponse;
import com.gitee.sqlrest.core.dto.ApiAssignmentDetailResponse;
import com.gitee.sqlrest.core.dto.ApiAssignmentSaveRequest;
import com.gitee.sqlrest.core.dto.ApiDebugExecuteRequest;
import com.gitee.sqlrest.core.dto.AssignmentSearchRequest;
import com.gitee.sqlrest.core.dto.DataTypeFormatMapValue;
import com.gitee.sqlrest.core.dto.NameValueRemarkResponse;
import com.gitee.sqlrest.core.dto.ScriptEditorCompletion;
import com.gitee.sqlrest.core.dto.SqlParamParseResponse;
import com.gitee.sqlrest.core.exec.ApiExecuteService;
import com.gitee.sqlrest.core.exec.annotation.Comment;
import com.gitee.sqlrest.core.exec.engine.ApiExecutorEngineFactory;
import com.gitee.sqlrest.core.exec.engine.impl.ScriptExecutorService;
import com.gitee.sqlrest.core.util.ApiPathUtils;
import com.gitee.sqlrest.core.util.DataSourceUtils;
import com.gitee.sqlrest.core.util.JacksonUtils;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.dao.DataSourceDao;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import com.gitee.sqlrest.persistence.entity.DataSourceEntity;
import com.gitee.sqlrest.persistence.util.PageUtils;
import com.gitee.sqlrest.template.Configuration;
import com.gitee.sqlrest.template.SqlTemplate;
import com.google.common.base.Charsets;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class ApiAssignmentService {

  private final static Map<String, List<ScriptEditorCompletion>> memCache = new ConcurrentHashMap<>();

  @Resource
  public ApiAssignmentDao apiAssignmentDao;
  @Resource
  private DataSourceDao dataSourceDao;
  @Resource
  private DriverLoadService driverLoadService;
  @Resource
  private ApiExecuteService apiExecuteService;

  public List<ScriptEditorCompletion> completions() {
    return memCache.computeIfAbsent("COMPLETION", this::computeCompletions);
  }

  private List<ScriptEditorCompletion> computeCompletions(String key) {
    List<ScriptEditorCompletion> results = new ArrayList<>();
    results.addAll(ScriptExecutorService.syntax);

    for (Class clazz : ScriptExecutorService.modules) {
      String varName = ScriptExecutorService.getModuleVarName(clazz);
      for (Method method : clazz.getMethods()) {
        if (method.isAnnotationPresent(Comment.class)) {
          String methodName = method.getName();
          String params = Stream.of(method.getParameters())
              .map(item -> {
                String type = item.getType().getSimpleName();
                String name = item.isAnnotationPresent(Comment.class)
                    ? item.getAnnotation(Comment.class).value()
                    : item.getName();
                return type + " " + name;
              })
              .collect(Collectors.joining(","));
          results.add(ScriptEditorCompletion.builder()
              .meta(method.getReturnType().getName())
              .caption(String.format("%s.%s(%s)", varName, methodName, params))
              .value(String.format("%s.%s( )", varName, methodName))
              .build());
        }
      }
    }

    return results;
  }

  public List<SqlParamParseResponse> parseSqlParams(String text) {
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(text);
    return template.getParameterNames().entrySet().stream()
        .map(e -> new SqlParamParseResponse(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  public void debugExecute(ApiDebugExecuteRequest request, HttpServletResponse response) {
    DataSourceEntity dataSourceEntity = dataSourceDao.getById(request.getDataSourceId());
    if (null == dataSourceEntity) {
      String message = "datasource[id=" + request.getDataSourceId() + " not exist!";
      log.warn("Error for debug, information:{}", message);
      throw new CommonException(ResponseErrorCode.ERROR_INTERNAL_ERROR, message);
    }
    if (CollectionUtils.isEmpty(request.getContextList())) {
      throw new CommonException(ResponseErrorCode.ERROR_INTERNAL_ERROR, "contextList invalid argument");
    }
    List<ApiContextEntity> scripts = request.getContextList().stream()
        .map(str -> ApiContextEntity.builder().sqlText(str).build())
        .collect(Collectors.toList());
    Map<String, Object> params = new HashMap<>();
    if (!CollectionUtils.isEmpty(request.getParamValues())) {
      Map<String, List<ParamValue>> names = request.getParamValues()
          .stream().collect(Collectors.groupingBy(ParamValue::getName));
      for (Map.Entry<String, List<ParamValue>> entry : names.entrySet()) {
        String paramName = entry.getKey();
        List<ParamValue> value = entry.getValue();
        ParamTypeEnum type = value.get(0).getType();
        if (value.size() > 1) {
          List<Object> values = value.stream().map(ParamValue::getValue)
              .map(s -> type.getConverter().apply(s))
              .collect(Collectors.toList());
          params.put(paramName, values);
        } else {
          params.put(paramName, type.getConverter().apply(value.get(0).getValue()));
        }
      }
    }

    if (null == request.getNamingStrategy()) {
      request.setNamingStrategy(NamingStrategyEnum.CAMEL_CASE);
    }

    File driverPath = driverLoadService.getVersionDriverFile(dataSourceEntity.getType(), dataSourceEntity.getVersion());

    ResultEntity entity;
    try {
      HikariDataSource dataSource = DataSourceUtils.getHikariDataSource(dataSourceEntity, driverPath.getAbsolutePath());
      Object result = ApiExecutorEngineFactory
          .getExecutor(request.getEngine(), dataSource, dataSourceEntity.getType())
          .execute(scripts, params, request.getNamingStrategy());
      if (result instanceof Collection) {
        Collection r = (Collection) result;
        result = scripts.size() == 1 ? r.stream().findFirst().get() : r;
      }
      entity = ResultEntity.success(result);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      entity = ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR, ExceptionUtil.getMessage(e));
    }

    try {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(Charsets.UTF_8.name());
      response.getWriter().append(JacksonUtils.toJsonStr(entity, request.getFormatMap().stream()
          .collect(Collectors.toMap(DataTypeFormatMapValue::getKey, DataTypeFormatMapValue::getValue, (a, b) -> a))));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Long createAssignment(ApiAssignmentSaveRequest request) {
    if (StringUtils.isBlank(request.getPath()) || null == request.getMethod()) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "path or method");
    }
    if (null != apiAssignmentDao.getByUk(request.getMethod(), request.getPath())) {
      String message = String.format("path=[%s]%s", request.getMethod().name(), request.getPath());
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, message);
    }
    if (CollectionUtils.isEmpty(request.getContextList())) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "contextList");
    }
    if (null == request.getNamingStrategy()) {
      request.setNamingStrategy(NamingStrategyEnum.CAMEL_CASE);
    }

    List<ApiContextEntity> contextList = getContextListEntity(request.getContextList());

    ApiAssignmentEntity assignmentEntity = new ApiAssignmentEntity();
    assignmentEntity.setGroupId(request.getGroupId());
    assignmentEntity.setModuleId(request.getModuleId());
    assignmentEntity.setDatasourceId(request.getDatasourceId());
    assignmentEntity.setName(request.getName());
    assignmentEntity.setDescription(request.getDescription());
    assignmentEntity.setMethod(request.getMethod());
    assignmentEntity.setPath(request.getPath());
    assignmentEntity.setContentType(request.getContentType());
    assignmentEntity.setParams(request.getParams());
    assignmentEntity.setOpen(Optional.ofNullable(request.getOpen()).orElse(false));
    assignmentEntity.setEngine(request.getEngine());
    assignmentEntity.setStatus(false);
    assignmentEntity.setContextList(contextList);
    assignmentEntity.setResponseFormat(request.getFormatMap().stream()
        .collect(Collectors.toMap(DataTypeFormatMapValue::getKey, DataTypeFormatMapValue::getValue, (a, b) -> a)));
    assignmentEntity.setNamingStrategy(request.getNamingStrategy());
    assignmentEntity.setFlowStatus(Optional.ofNullable(request.getFlowStatus()).orElse(false));
    assignmentEntity.setFlowGrade(request.getFlowGrade());
    assignmentEntity.setFlowCount(request.getFlowCount());

    apiAssignmentDao.insert(assignmentEntity);
    return assignmentEntity.getId();
  }

  public void updateAssignment(ApiAssignmentSaveRequest request) {
    ApiAssignmentEntity exists = apiAssignmentDao.getById(request.getId(), false);
    if (null == exists) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + request.getId());
    }
    if (exists.getStatus()) {
      throw new CommonException(ResponseErrorCode.ERROR_EDIT_ALREADY_PUBLISHED, "id=" + request.getId());
    }
    if (StringUtils.isBlank(request.getPath()) || null == request.getMethod()) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "path or method");
    }
    if (CollectionUtils.isEmpty(request.getContextList())) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "sqlTextList");
    }

    if (null == request.getNamingStrategy()) {
      request.setNamingStrategy(NamingStrategyEnum.CAMEL_CASE);
    }

    List<ApiContextEntity> contextList = getContextListEntity(request.getContextList());

    ApiAssignmentEntity assignmentEntity = new ApiAssignmentEntity();
    assignmentEntity.setId(request.getId());
    assignmentEntity.setGroupId(request.getGroupId());
    assignmentEntity.setModuleId(request.getModuleId());
    assignmentEntity.setDatasourceId(request.getDatasourceId());
    assignmentEntity.setName(request.getName());
    assignmentEntity.setDescription(request.getDescription());
    //assignmentEntity.setMethod(request.getMethod());
    //assignmentEntity.setPath(request.getPath());
    assignmentEntity.setContentType(request.getContentType());
    assignmentEntity.setParams(request.getParams());
    assignmentEntity.setOpen(Optional.ofNullable(request.getOpen()).orElse(false));
    assignmentEntity.setStatus(false);
    assignmentEntity.setEngine(request.getEngine());
    assignmentEntity.setContextList(contextList);
    assignmentEntity.setResponseFormat(request.getFormatMap().stream()
        .collect(Collectors.toMap(DataTypeFormatMapValue::getKey, DataTypeFormatMapValue::getValue, (a, b) -> a)));
    assignmentEntity.setNamingStrategy(request.getNamingStrategy());
    assignmentEntity.setFlowStatus(Optional.ofNullable(request.getFlowStatus()).orElse(false));
    assignmentEntity.setFlowGrade(request.getFlowGrade());
    assignmentEntity.setFlowCount(request.getFlowCount());

    apiAssignmentDao.update(assignmentEntity);
  }

  public ApiAssignmentDetailResponse detailAssignment(Long id) {
    ApiAssignmentEntity assignmentEntity = apiAssignmentDao.getById(id, true);
    if (null == assignmentEntity) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }

    ApiAssignmentDetailResponse response = new ApiAssignmentDetailResponse();
    BeanUtil.copyProperties(assignmentEntity, response);
    response.setSqlList(assignmentEntity.getContextList());
    List<DataTypeFormatMapValue> formatMap = new ArrayList<>();

    for (Map.Entry<DataTypeFormatEnum, String> entry : assignmentEntity.getResponseFormat().entrySet()) {
      formatMap.add(
          DataTypeFormatMapValue.builder()
              .key(entry.getKey())
              .value(entry.getValue())
              .remark(entry.getKey().getClassName())
              .build());
    }
    response.setFormatMap(formatMap);
    return response;
  }

  public void testAssignment(Long id, HttpServletRequest request, HttpServletResponse response) {
    ApiAssignmentEntity assignmentEntity = apiAssignmentDao.getById(id, true);
    if (null == assignmentEntity) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }

    String json;
    ResultEntity result = apiExecuteService.execute(assignmentEntity, request);
    if (0 != result.getCode()) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      json = JacksonUtils.toJsonStr(ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR));
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      json = JacksonUtils.toJsonStr(result, assignmentEntity.getResponseFormat());
    }

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(Charsets.UTF_8.name());
    try {
      response.getWriter().append(json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteAssignment(Long id) {
    ApiAssignmentEntity assignmentEntity = apiAssignmentDao.getById(id, false);
    if (null != assignmentEntity) {
      if (OnOffEnum.ON.equals(assignmentEntity.getStatus())) {
        throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_USED, "api assignment is online.");
      }
      apiAssignmentDao.deleteById(id);
    }
  }

  public void makeOpen(Long id, Boolean open) {
    apiAssignmentDao.makeOpen(id, open);
  }

  public void deployAssignment(Long id) {
    apiAssignmentDao.updateStatus(id, true);
  }

  public void retireAssignment(Long id) {
    apiAssignmentDao.updateStatus(id, false);
  }

  public PageResult<ApiAssignmentBaseResponse> listAll(AssignmentSearchRequest request) {
    Supplier<List<ApiAssignmentBaseResponse>> method = () -> {
      List<ApiAssignmentEntity> lists = apiAssignmentDao
          .listAll(request.getGroupId(), request.getModuleId(),
              request.getPublish(), request.getOpen(),
              request.getSearchText());
      return lists.stream()
          .map(assignmentEntity -> {
            ApiAssignmentBaseResponse response = new ApiAssignmentBaseResponse();
            BeanUtil.copyProperties(assignmentEntity, response);
            response.setPath(ApiPathUtils.getFullPath(response.getPath()));
            return response;
          })
          .collect(Collectors.toList());
    };

    return PageUtils.getPage(method, request.getPage(), request.getSize());
  }

  private List<ApiContextEntity> getContextListEntity(List<String> contextList) {
    List<ApiContextEntity> sqlList = contextList.stream()
        .filter(StringUtils::isNotBlank)
        .map(s -> new ApiContextEntity(s))
        .collect(Collectors.toList());
    return sqlList;
  }

}
