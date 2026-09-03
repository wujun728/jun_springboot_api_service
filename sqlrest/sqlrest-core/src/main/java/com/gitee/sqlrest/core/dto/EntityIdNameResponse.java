package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("带ID的名称")
public class EntityIdNameResponse {

  @ApiModelProperty("ID编号")
  private Long id;

  @ApiModelProperty("名称")
  private String name;
}
