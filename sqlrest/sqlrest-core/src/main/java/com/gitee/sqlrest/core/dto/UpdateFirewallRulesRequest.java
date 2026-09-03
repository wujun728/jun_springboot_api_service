package com.gitee.sqlrest.core.dto;

import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.common.enums.WhiteBlackEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("防火墙规则")
public class UpdateFirewallRulesRequest {

  @ApiModelProperty("开启状态")
  private OnOffEnum status;

  @ApiModelProperty("黑白名单选项")
  private WhiteBlackEnum mode;

  @ApiModelProperty("地址列表")
  private String addresses;
}
