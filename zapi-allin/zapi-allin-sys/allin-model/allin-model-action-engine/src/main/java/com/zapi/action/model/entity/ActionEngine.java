package com.zapi.action.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动作执行引擎实体类
 * 
 * @author zapi
 * @date 2025-01-12
 */
@Data
@TableName("action_engine")
public class ActionEngine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作描述
     */
    private String actionDescription;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 动作配置（JSON格式）
     */
    private String actionConfig;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableLogic
    private Integer delFlag;
}
