package com.gitee.sqlrest.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sqlrest.common.enums.ExecuteEngineEnum;
import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.common.enums.OnOffEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("API配置简单详情")
public class ApiAssignmentBaseResponse {

  @ApiModelProperty("ID编号")
  private Long id;

  @ApiModelProperty("API配置名称")
  private String name;

  @ApiModelProperty("API请求方法:GET, HEAD, PUT, POST, DELETE")
  private HttpMethodEnum method;

  @ApiModelProperty("请求路径(不带api前缀)")
  private String path;

  @ApiModelProperty("是否发布")
  private Boolean status;

  @ApiModelProperty("是否公开")
  private Boolean open;

  @ApiModelProperty("执行引擎")
  private ExecuteEngineEnum engine;

  @ApiModelProperty("创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp createTime;

  @ApiModelProperty("更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Timestamp updateTime;
}
