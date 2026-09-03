package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("列表搜索")
public class AssignmentSearchRequest extends EntitySearchRequest {

  @ApiModelProperty("分组ID")
  private Long groupId;

  @ApiModelProperty("模块ID")
  private Long moduleId;

  @ApiModelProperty("是否发布")
  private Boolean publish;

  @ApiModelProperty("是否公开")
  private Boolean open;

}
