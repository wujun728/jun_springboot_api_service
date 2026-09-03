package com.gitee.sqlrest.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("参数信息")
public class ParamValue extends ItemParam {

  @ApiModelProperty("参数值")
  private String value;
}
