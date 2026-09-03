package com.gitee.sqlrest.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("按照日期的统计")
public class DateCount {

  @ApiModelProperty("日期")
  private String ofDate;

  @ApiModelProperty("总数")
  private Long total;

  @ApiModelProperty("成功数")
  private Long success;
}
