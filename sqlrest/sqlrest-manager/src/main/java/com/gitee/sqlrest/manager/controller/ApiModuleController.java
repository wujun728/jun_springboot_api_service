package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.service.ApiModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"模块管理接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/module")
public class ApiModuleController {

  @Resource
  private ApiModuleService apiModuleService;

  @ApiOperation(value = "添加模块")
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity create(@RequestParam("name") String name) {
    apiModuleService.createModule(name);
    return ResultEntity.success();
  }

  @ApiOperation(value = "更新模块")
  @PostMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity update(@PathVariable("id") Long id, @RequestParam("name") String name) {
    apiModuleService.updateModule(id, name);
    return ResultEntity.success();
  }

  @ApiOperation(value = "删除模块")
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity delete(@PathVariable("id") Long id) {
    apiModuleService.deleteModule(id);
    return ResultEntity.success();
  }

  @ApiOperation(value = "模块列表")
  @PostMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity listAll() {
    return ResultEntity.success(apiModuleService.listAll());
  }
}
