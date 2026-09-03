package com.zapi.workflow.service.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 流程撤回请求DTO
 * 用于发起人撤回尚未结束的流程实例
 */
@Data
@ApiModel(description = "流程撤回请求参数")
public class RevokeProcessDTO {

    /**
     * 流程实例ID
     * 注意：此字段由Controller从路径参数设置，不需要前端传递
     */
    @ApiModelProperty(value = "流程实例ID")
    private String instanceId;

    /**
     * 撤回原因
     */
    @ApiModelProperty(value = "撤回原因", required = true)
    @NotBlank(message = "撤回原因不能为空")
    private String reason;

    /**
     * 操作人（发起人）
     */
    @ApiModelProperty(value = "操作人ID", required = true)
    @NotBlank(message = "操作人不能为空")
    private String operator;
}
