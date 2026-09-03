package com.gitee.sqlrest.core.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitee.sqlrest.common.dto.ItemParam;
import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("API配置详细详情")
public class ApiAssignmentDetailResponse extends ApiAssignmentBaseResponse {

  @ApiModelProperty("分组ID")
  private Long groupId;

  @ApiModelProperty("模块ID")
  private Long moduleId;

  @ApiModelProperty("数据源ID")
  private Long datasourceId;

  @ApiModelProperty("描述")
  private String description;

  @ApiModelProperty("接口入参")
  private List<ItemParam> params;

  @ApiModelProperty("HTTP请求的contentType")
  private String contentType;

  @ApiModelProperty("SQL列表")
  private List<ApiContextEntity> sqlList;

  @ApiModelProperty("接口出参数据类型转换格式")
  private List<DataTypeFormatMapValue> formatMap;

  @ApiModelProperty("接口出参属性命名策略")
  private NamingStrategyEnum namingStrategy;

  @ApiModelProperty("是否开启流量控制")
  private Boolean flowStatus;

  @ApiModelProperty("阈值类型")
  private Integer flowGrade;

  @TableField("阈值大小")
  private Integer flowCount;
}
