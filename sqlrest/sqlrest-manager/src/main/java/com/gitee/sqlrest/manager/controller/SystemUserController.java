package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.dto.SystemUserDetailResponse;
import com.gitee.sqlrest.core.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"用户管理接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/user")
public class SystemUserController {

  @Resource
  private SystemUserService systemUserService;

  @ApiOperation(value = "用户详情", notes = "根据用户ID获取用户详情")
  @GetMapping(value = "/detail/id", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<SystemUserDetailResponse> getUserById(@RequestParam("id") Long id) {
    return ResultEntity.success(systemUserService.getUserDetailById(id));
  }

  @ApiOperation(value = "用户详情", notes = "根据用户名获取用户详情")
  @GetMapping(value = "/detail/name", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<SystemUserDetailResponse> getUserByName(@RequestParam("username") String username) {
    return ResultEntity.success(systemUserService.getUserDetailByUsername(username));
  }

  @ApiOperation(value = "修改密码", notes = "用户修改自己的密码")
  @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity changeOwnPassword(HttpServletRequest request,
      @RequestParam("oldPassword") String oldPassword,
      @RequestParam("newPassword") String newPassword) {
    systemUserService.changeOwnPassword(request, oldPassword, newPassword);
    return ResultEntity.success();
  }

}
