package com.zapi.workflow.service.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程定义分页查询DTO
 * 用于流程定义列表的条件查询和分页
 */
@Data
@ApiModel("流程定义分页查询参数")
public class ProcessDefinitionQueryDTO {
    
    /**
     * 流程名称
     * 支持模糊查询
     */
    @ApiModelProperty("流程名称（支持模糊查询）")
    private String name;
    
    /**
     * 流程标识
     * 支持模糊查询
     */
    @ApiModelProperty("流程标识（支持模糊查询）")
    private String key;
    
    /**
     * 流程状态
     * 1：激活状态
     * 0：禁用状态
     * 不传则查询所有状态
     */
    @ApiModelProperty("流程状态（1-激活，0-禁用）")
    private Integer status;
    
    /**
     * 业务类型
     * 1：系统表单
     * 2：业务表单
     * 不传则查询所有类型
     */
    @ApiModelProperty("业务类型（1-系统表单，2-业务表单）")
    private Integer businessType;
    
    /**
     * 应用ID
     * 用于查询指定应用下的流程定义
     */
    @ApiModelProperty("应用ID")
    private String appId;
    
    /**
     * 页码
     * 从1开始，默认为1
     */
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     * 默认为10，最大不超过100
     */
    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}
