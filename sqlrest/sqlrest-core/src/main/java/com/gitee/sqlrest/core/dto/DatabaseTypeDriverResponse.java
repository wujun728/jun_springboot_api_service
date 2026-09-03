package com.gitee.sqlrest.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("驱动版本")
public class DatabaseTypeDriverResponse {

  @ApiModelProperty("驱动版本")
  private String driverVersion;

  @ApiModelProperty("驱动类名")
  private String driverClass;

  @ApiModelProperty("版本路径")
  private String driverPath;

  @ApiModelProperty("驱动JAR")
  private List<String> jarFiles;
}
