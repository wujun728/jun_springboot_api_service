package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("列信息")
public class MetadataColumnResponse {

  @ApiModelProperty("列名")
  private String name;

  @ApiModelProperty("列类型")
  private String type;

  @ApiModelProperty("注释")
  private String remarks;
}
