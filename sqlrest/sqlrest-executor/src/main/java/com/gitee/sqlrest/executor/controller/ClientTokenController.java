package com.gitee.sqlrest.executor.controller;

import com.gitee.sqlrest.common.dto.AccessToken;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.servlet.ClientTokenService;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class ClientTokenController {

  @Resource
  private ClientTokenService clientTokenService;

  @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<AccessToken> generateToken(String clientId, String secret) {
    AccessToken token = clientTokenService.generateToken(clientId, secret);
    if (null == token) {
      return ResultEntity.failed(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS);
    }
    return ResultEntity.success(token);
  }
}
