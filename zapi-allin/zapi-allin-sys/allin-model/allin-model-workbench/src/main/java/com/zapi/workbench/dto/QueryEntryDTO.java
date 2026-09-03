package com.zapi.workbench.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName QueryEntryDTO
 * @Author zhonglz~
 * @create 2023/4/17 16:03
 */
@Data
@ApiModel(value="QueryEntryDTO", description="查询条目")
public class QueryEntryDTO {

    @ApiModelProperty(value = "应用ID")
    @NotBlank(message = "应用ID不能为空")
    private Long appId;

    @ApiModelProperty(value = "条目名称")
    private String name;

    @ApiModelProperty(value = "类型集合",hidden = true)
    private List<String> types;

    @ApiModelProperty(value = "是否是流程表单0普通表单1流程表单",hidden = true)
    private Integer hasFlow;

    @ApiModelProperty(value = "上级ID",hidden = true)
    private Long parentId;

    @ApiModelProperty(value = "是否包含分组列表信息，空值或者等于0包含分组信息，1不包含")
    private Integer isGroup;

    @ApiModelProperty(value = "根据sort倒序排序",hidden = true)
    private Boolean isSortAsc;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

}
