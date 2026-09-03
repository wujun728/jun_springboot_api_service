package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.service.ApiGroupService;
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

@Api(tags = {"分组管理接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/group")
public class ApiGroupController {

  @Resource
  private ApiGroupService apiGroupService;

  @ApiOperation(value = "添加分组")
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity create(@RequestParam("name") String name) {
    apiGroupService.createGroup(name);
    return ResultEntity.success();
  }

  @ApiOperation(value = "更新分组")
  @PostMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity update(@PathVariable("id") Long id, @RequestParam("name") String name) {
    apiGroupService.updateGroup(id, name);
    return ResultEntity.success();
  }

  @ApiOperation(value = "删除分组")
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity delete(@PathVariable("id") Long id) {
    apiGroupService.deleteGroup(id);
    return ResultEntity.success();
  }

  @ApiOperation(value = "分组列表")
  @PostMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity listAll() {
    return ResultEntity.success(apiGroupService.listAll());
  }
}
