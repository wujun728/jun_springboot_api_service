package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 节点位置传输对象
 * 用于流程图中节点的坐标定位
 */
@Data
public class PositionDTO {
    
    /**
     * X坐标
     * 必填字段，节点在流程图中的水平位置
     * 单位：像素，相对于画布左上角
     */
    @NotNull(message = "X坐标不能为空")
    private Integer x;
    
    /**
     * Y坐标
     * 必填字段，节点在流程图中的垂直位置
     * 单位：像素，相对于画布左上角
     */
    @NotNull(message = "Y坐标不能为空")
    private Integer y;
}
    