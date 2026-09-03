package com.zapi.workbench.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 拖拽排序DTO
 */
@Data
@ApiModel("拖拽排序DTO")
public class DragEntryDTO {

    @ApiModelProperty(value = "被拖拽的条目ID", required = true)
    @NotNull(message = "被拖拽的条目ID不能为空")
    private Long entryId;

    @ApiModelProperty(value = "目标父级ID（可选，如果不传则保持原父级）")
    private Long parentId;

    @ApiModelProperty(value = "前驱节点ID（拖拽至某个分组的首位则填充0）", required = true)
    @NotNull(message = "前驱节点ID不能为空")
    private Long beforeEntryId;
}
