package com.zapi.workbench.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 外部应用实体类
 * 用于管理对外开放的应用接口
 */
@Data
@TableName("wb_application")
public class WbApplication {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用图标URL
     */
    private String appIcon;

    /**
     * API授权码（系统自动生成，不可修改）
     */
    private String authCode;

    /**
     * API授权密码（系统自动生成，不可修改）
     */
    private String authPsw;

    /**
     * 状态：0-禁用 1-启用
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
     * 创建人
     */
    private String creator;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 备注
     */
    private String remark;
}