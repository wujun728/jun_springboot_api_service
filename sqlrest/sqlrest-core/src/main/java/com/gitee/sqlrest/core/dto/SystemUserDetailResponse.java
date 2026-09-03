package com.gitee.sqlrest.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("系统用户")
public class SystemUserDetailResponse {

  @ApiModelProperty("ID编号")
  private Long id;

  @ApiModelProperty("登陆名")
  private String username;

  @ApiModelProperty("实际名")
  private String realName;

  @ApiModelProperty("电子邮箱")
  private String email;

  @ApiModelProperty("地址")
  private String address;

  @ApiModelProperty("是否锁定")
  private Boolean locked;

  @ApiModelProperty("创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp createTime;

  @ApiModelProperty("更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp updateTime;

}