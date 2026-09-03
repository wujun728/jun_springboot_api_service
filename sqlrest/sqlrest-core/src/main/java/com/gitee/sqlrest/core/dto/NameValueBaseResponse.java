package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("枚举键值")
public class NameValueBaseResponse {

  @ApiModelProperty("枚举至")
  private Enum key;

  @ApiModelProperty("说明")
  private String value;
}
