package com.zapi.workbench.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用条目实体类
 * 用于管理应用中的目录、表单、表格等条目
 */
@Data
@TableName("wb_app_entry")
public class WbAppEntry {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 目录名称
     */
    private String name;

    /**
     * 目录类型：分组group 表单form 表格grid
     */
    private String type;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 图标颜色
     */
    private String color;

    /**
     * 排序字段（存相邻前驱节点ID）
     */
    private Integer sort;

    /**
     * 是否是流程表单：0否 1是（流程和普通表单切换，默认普通表单）
     */
    private Integer hasFlow;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 是否删除：0正常，1删除
     */
    @TableLogic
    private Integer isDeleted;
}
