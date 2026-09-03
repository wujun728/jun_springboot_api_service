package com.gitee.sqlrest.core.dto;

import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("数据源保存")
public class DataSourceSaveRequest {

  @ApiModelProperty("ID编号")
  private Long id;

  @ApiModelProperty("名称")
  private String name;

  @ApiModelProperty("类型")
  private ProductTypeEnum type;

  @ApiModelProperty("驱动版本")
  private String version;

  @ApiModelProperty("驱动类型")
  private String driver;

  @ApiModelProperty("连接JDBC-URL")
  private String url;

  @ApiModelProperty("账号")
  private String username;

  @ApiModelProperty("密码")
  private String password;
}
