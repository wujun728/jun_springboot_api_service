package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.service.SystemParamService;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"参数管理接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/param")
public class SystemParamController {

  @Resource
  private SystemParamService systemParamService;

  @GetMapping(value = "/value/query", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity getByParamKey(@RequestParam("key") String key) {
    return ResultEntity.success(systemParamService.getByParamKey(key));
  }

  @PostMapping(value = "/value/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity updateByParamKey(@RequestParam("key") String key,
      @RequestParam("value") String value) {
    systemParamService.updateByParamKey(key, value);
    return ResultEntity.success();
  }
}
