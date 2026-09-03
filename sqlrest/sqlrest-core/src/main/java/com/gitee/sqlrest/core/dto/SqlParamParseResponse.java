package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("SQL参数解析")
public class SqlParamParseResponse {

  @ApiModelProperty("参数名")
  private String name;

  @ApiModelProperty("是否为数组")
  private Boolean isArray;
}
