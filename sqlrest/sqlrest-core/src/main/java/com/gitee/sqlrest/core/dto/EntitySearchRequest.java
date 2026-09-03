package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("列表搜索")
public class EntitySearchRequest {

  @ApiModelProperty("页号")
  private Integer page;

  @ApiModelProperty("页大小")
  private Integer size;

  @ApiModelProperty("关键词")
  private String searchText;
  
}
