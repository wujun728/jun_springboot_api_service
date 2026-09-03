package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.PageResult;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.dto.AppClientDetailResponse;
import com.gitee.sqlrest.core.dto.AppClientGroupRequest;
import com.gitee.sqlrest.core.dto.AppClientSaveRequest;
import com.gitee.sqlrest.core.dto.EntityIdNameResponse;
import com.gitee.sqlrest.core.dto.EntitySearchRequest;
import com.gitee.sqlrest.core.service.AppClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"客户端应用接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/client")
public class AppClientController {

  @Resource
  private AppClientService appClientService;

  @ApiOperation(value = "添加客户端应用")
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity create(@RequestBody AppClientSaveRequest request) {
    appClientService.create(request);
    return ResultEntity.success();
  }

  @ApiOperation(value = "删除客户端应用")
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity delete(@PathVariable("id") Long id) {
    appClientService.delete(id);
    return ResultEntity.success();
  }

  @ApiOperation(value = "客户端应用列表")
  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<AppClientDetailResponse> searchList(@RequestBody EntitySearchRequest request) {
    return appClientService.searchList(request);
  }

  @ApiOperation(value = "查询应用密钥")
  @GetMapping(value = "/secret/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<String> secret(@PathVariable("id") Long id) {
    return ResultEntity.success(appClientService.getSecret(id));
  }

  @ApiOperation(value = "创建分组关联")
  @PostMapping(value = "/auth/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity createGroupAuth(@RequestBody AppClientGroupRequest request) {
    appClientService.createGroupAuth(request);
    return ResultEntity.success();
  }

  @ApiOperation(value = "查询分组关联")
  @GetMapping(value = "/auth/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<EntityIdNameResponse> getGroupAuth(@PathVariable("id") Long id) {
    return appClientService.getGroupAuth(id);
  }
}
