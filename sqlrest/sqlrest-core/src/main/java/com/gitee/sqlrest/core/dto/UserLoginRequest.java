package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("账号登陆请求")
public class UserLoginRequest {

  @ApiModelProperty("账号")
  private String username;

  @ApiModelProperty("密码")
  private String password;
}
