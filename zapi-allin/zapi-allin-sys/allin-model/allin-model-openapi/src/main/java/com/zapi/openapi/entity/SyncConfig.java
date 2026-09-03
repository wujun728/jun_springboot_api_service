package com.zapi.openapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据同步配置实体类
 * 用于存储字段映射配置
 */
@Data
@TableName("sys_sync_config")
public class SyncConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 同步类型：user-用户, role-角色, org-机构
     */
    private String syncType;

    /**
     * 字段映射配置（JSON格式）
     */
    private String fieldMapping;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}