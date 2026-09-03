package com.zapi.grid.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 表格实体类
 *
 * @author zapi
 * @since 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("grid_base_info")
public class Grid {

    /**
     * 表格ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 表格名称
     */
    @TableField("grid_name")
    private String gridName;

    /**
     * 表格描述
     */
    @TableField("grid_description")
    private String gridDescription;

    /**
     * 表格配置JSON
     */
    @TableField("grid_config")
    private String gridConfig;

    /**
     * 应用条目ID
     */
    @TableField("entry_id")
    private Long entryId;

    /**
     * 表格状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag;
}
