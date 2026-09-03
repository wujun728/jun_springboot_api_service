package com.zapi.form.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 表单实体类
 *
 * @author zapi
 * @since 2024-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("form_base_info")
public class Form {

    /**
     * 表单ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 表单名称
     */
    @TableField("form_name")
    private String formName;

    /**
     * 表单描述
     */
    @TableField("form_description")
    private String formDescription;

    /**
     * 应用目录ID
     */
    @TableField("entry_id")
    private Long entryId;

    /**
     * 表单配置JSON
     */
    @TableField("form_config")
    private String formConfig;

    /**
     * 表单状态：0-禁用，1-启用
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
