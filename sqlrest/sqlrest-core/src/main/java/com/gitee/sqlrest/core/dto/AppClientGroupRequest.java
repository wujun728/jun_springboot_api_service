package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("客户端分组关联")
public class AppClientGroupRequest {

  @ApiModelProperty("客户端应用ID")
  private Long id;

  @ApiModelProperty("分组ID列表")
  private List<Long> groupIds;
}
