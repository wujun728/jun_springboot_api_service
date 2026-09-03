package com.gitee.sqlrest.common.dto;

import com.gitee.sqlrest.common.enums.ParamLocationEnum;
import com.gitee.sqlrest.common.enums.ParamTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("参数信息")
public class ItemParam {

  @ApiModelProperty("参数名")
  private String name;

  @ApiModelProperty("参数位置")
  private ParamLocationEnum location;

  @ApiModelProperty("参数类型")
  private ParamTypeEnum type;

  @ApiModelProperty("是否为数组")
  private Boolean isArray;

  @ApiModelProperty("是否必填")
  private Boolean required;

  @ApiModelProperty("默认值")
  private String defaultValue;

  @ApiModelProperty("参数描述")
  private String remark;
}
