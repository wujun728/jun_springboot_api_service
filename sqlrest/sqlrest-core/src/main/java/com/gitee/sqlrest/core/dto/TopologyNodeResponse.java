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
@ApiModel("拓扑节点信息")
public class TopologyNodeResponse {

  @ApiModelProperty("服务ID")
  private String serviceId;

  @ApiModelProperty("实例ID")
  private String instanceId;

  @ApiModelProperty("主机地址")
  private String host;

  @ApiModelProperty("端口号")
  private Integer port;
}
