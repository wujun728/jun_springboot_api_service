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
@ApiModel("脚本编辑器提示列表")
public class ScriptEditorCompletion {

  @ApiModelProperty("返回值类型")
  private String meta;

  @ApiModelProperty("下拉提示")
  private String caption;

  @ApiModelProperty("选择填充")
  private String value;

  @Builder.Default
  @ApiModelProperty("分数值")
  private Integer score = 1;
}
