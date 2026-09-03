package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典数据实体类
 */
@Data
@TableName("sys_dict_data")
@ApiModel(value = "SysDictData", description = "字典数据")
public class SysDictData {

    /**
     * 字典编码
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "字典编码")
    private Long dictCode;

    /**
     * 字典排序
     */
    @ApiModelProperty(value = "字典排序")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @ApiModelProperty(value = "样式属性")
    private String cssClass;

    /**
     * 表格回显样式
     */
    @ApiModelProperty(value = "表格回显样式")
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @ApiModelProperty(value = "是否默认（Y是 N否）")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识（0未删除 1已删除）
     */
    @TableLogic
    @ApiModelProperty(value = "逻辑删除标识")
    private Integer deleted;
}