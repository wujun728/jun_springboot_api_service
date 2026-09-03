package com.gitee.sqlrest.executor.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.core.dto.SwaggerEntity;
import com.gitee.sqlrest.core.servlet.ApiSwaggerService;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Swagger接口文档"})
@RestController
@RequestMapping(value = Constants.API_DOC_PATH_PREFIX)
public class ApiSwaggerController {

  @Resource
  private ApiSwaggerService apiSwaggerService;

  @GetMapping(value = "/swagger.json", produces = MediaType.APPLICATION_JSON_VALUE)
  public SwaggerEntity getSwaggerJson(HttpServletRequest request) {
    return apiSwaggerService.getSwaggerJson(request);
  }
}
