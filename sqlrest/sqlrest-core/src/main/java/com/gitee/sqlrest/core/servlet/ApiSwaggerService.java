package com.gitee.sqlrest.core.servlet;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ItemParam;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.dto.SwaggerEntity;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.dao.ApiModuleDao;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.gitee.sqlrest.persistence.entity.ApiModuleEntity;
import com.google.common.collect.Lists;
import com.hazelcast.com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * https://juejin.cn/post/6996227860058865700
 */
@Service
public class ApiSwaggerService {

  private static final String INFO_TITLE = "SQLREST在线接口文档";
  private static final String INFO_VERSION = "1.0";
  private static final String TOKEN_MODEL = "TOKEN认证授权";
  private static final String BASE_PATH = "/";
  private static final String VAR_NAME_QUERY = "query";
  private static final String BODY_EMPTY = "{}";

  @Resource
  private ApiAssignmentDao apiAssignmentDao;
  @Resource
  private ApiModuleDao apiModuleDao;

  private String getApiUrlPrefix() {
    return String.format("/%s/", Constants.API_PATH_PREFIX);
  }

  public SwaggerEntity getSwaggerJson(HttpServletRequest request) {
    SwaggerEntity.Info info = new SwaggerEntity.Info(INFO_TITLE, INFO_VERSION, INFO_TITLE, null, null);
    Map<String, Object> securityDefinitionMap = new HashMap<>();
    Map<String, Object> securityMap = new HashMap<>();

    // Bearer Token
    securityDefinitionMap.put(SwaggerEntity.BearerAuth.KEY_NAME, new SwaggerEntity.BearerAuth());
    securityMap.put(SwaggerEntity.BearerAuth.KEY_NAME, new String[]{});

    SwaggerEntity swaggerEntity = new SwaggerEntity();
    swaggerEntity.setInfo(info);
    swaggerEntity.setSchemes(Lists.newArrayList("http"));
    swaggerEntity.setBasePath(BASE_PATH);
    swaggerEntity.addSecurityDefinitions(securityDefinitionMap);
    swaggerEntity.addSecurity(securityMap);

    List<ApiModuleEntity> moduleEntities = apiModuleDao.listAll();
    Map<Long, ApiModuleEntity> moduleIdMap = moduleEntities.stream()
        .collect(Collectors.toMap(ApiModuleEntity::getId,
            Function.identity(), (a, b) -> a));

    moduleEntities.forEach(module -> swaggerEntity.addTag(module.getName(), module.getName()));
    swaggerEntity.addTag(TOKEN_MODEL, TOKEN_MODEL);

    String urlPrefix = getApiUrlPrefix();

    for (ApiAssignmentEntity assignment : apiAssignmentDao.listAll()) {
      if (!assignment.getStatus()) {
        // 过滤掉未发布的
        continue;
      }
      String path = urlPrefix + assignment.getPath();
      String method = assignment.getMethod().name();
      SwaggerEntity.Path pathInfo = new SwaggerEntity.Path(String.valueOf(assignment.getId()));
      pathInfo.addTag(moduleIdMap.get(assignment.getModuleId()).getName());
      pathInfo.addProduce(assignment.getContentType());
      pathInfo.setSummary(assignment.getName());
      pathInfo.setDescription(StringUtils.defaultIfBlank(assignment.getDescription(), assignment.getName()));
      pathInfo.addProduce(MediaType.APPLICATION_JSON_VALUE);

      // 入参
      List<ItemParam> params = assignment.getParams();
      if (!CollectionUtils.isEmpty(params)) {
        for (ItemParam param : params) {
          boolean required = param.getRequired();
          String name = param.getName();
          String type = param.getType().getJsType();
          String description = param.getRemark();
          Map<String, Object> mapParams = SwaggerEntity
              .createParameter(required, name, VAR_NAME_QUERY, type, description, param.getType().getExample());
          pathInfo.addParameter(mapParams);
        }
      }

      // 响应
      String unique = assignment.getId().toString();
      pathInfo.addResponse("200", "OK", ImmutableMap.of("$ref", "#/definitions/" + unique));
      pathInfo.addResponse("401", ResultEntity.failed(ResponseErrorCode.ERROR_TOKEN_EXPIRED));
      pathInfo.addResponse("403", ResultEntity.failed(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN));
      pathInfo.addResponse("404", ResultEntity.failed(ResponseErrorCode.ERROR_PATH_NOT_EXISTS));
      swaggerEntity.addDefinitions(unique,
          ImmutableMap.of("type", "object",
              "properties", Collections.emptyMap(),
              "title", unique + "|" + assignment.getName())
      );

      swaggerEntity.addPath(path, method, pathInfo);
    }

    //token获取接口
    String path = "/token/generate";
    String method = "POST";
    SwaggerEntity.Path pathInfo = new SwaggerEntity.Path("0");
    pathInfo.addTag(TOKEN_MODEL);
    pathInfo.addProduce(MediaType.APPLICATION_JSON_VALUE);
    pathInfo.setSummary(TOKEN_MODEL);
    pathInfo.setDescription(TOKEN_MODEL);
    Map<String, Object> mapParamsUser = SwaggerEntity
        .createParameter(true, "clientId", VAR_NAME_QUERY, "string", "账号", null);
    pathInfo.addParameter(mapParamsUser);
    Map<String, Object> mapParamsPwd = SwaggerEntity
        .createParameter(true, "secret", VAR_NAME_QUERY, "string", "密码", null);
    pathInfo.addParameter(mapParamsPwd);
    pathInfo.addResponse("200", BODY_EMPTY);
    swaggerEntity.addPath(path, method, pathInfo);

    return swaggerEntity;
  }
}
