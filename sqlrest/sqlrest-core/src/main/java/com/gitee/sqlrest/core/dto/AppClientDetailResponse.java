package com.gitee.sqlrest.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sqlrest.common.enums.DurationTimeEnum;
import com.gitee.sqlrest.common.enums.ExpireTimeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("客户端应用详情")
public class AppClientDetailResponse {

  @ApiModelProperty("ID编号")
  private Long id;

  @ApiModelProperty("客户端应用名称")
  private String name;

  @ApiModelProperty("客户端应用描述")
  private String description;

  @ApiModelProperty("应用AppKey账号")
  private String appKey;

  @ApiModelProperty("到期类型")
  private DurationTimeEnum expireDuration;

  @ApiModelProperty("到期时间")
  private Long expireAt;

  @ApiModelProperty("到期时间(字符串)")
  private String expireAtStr;

  @ApiModelProperty("过期类型")
  private String expireType;

  @ApiModelProperty("是否过期")
  private Boolean isExpired;

  @ApiModelProperty("创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp createTime;

  @ApiModelProperty("更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp updateTime;
}
