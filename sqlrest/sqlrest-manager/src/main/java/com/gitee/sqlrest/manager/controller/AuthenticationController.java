package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.dto.UserLoginRequest;
import com.gitee.sqlrest.core.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"登陆认证接口"})
@RestController
@RequestMapping(value = "/user")
public class AuthenticationController {

  @Resource
  private SystemUserService systemUserService;

  @ApiOperation(value = "账号登录", notes = "使用一个账号密码登录")
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity login(@RequestBody UserLoginRequest request) {
    return ResultEntity.success(systemUserService.login(request.getUsername(), request.getPassword()));
  }

  @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "认证登出", notes = "登出系统")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)
  })
  public ResultEntity logout(HttpServletRequest request) {
    systemUserService.logout(request);
    return ResultEntity.success();
  }
}
