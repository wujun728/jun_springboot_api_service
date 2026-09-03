package com.zapi.openapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 开放接口配置实体类
 */
@Data
@TableName("open_api_config")
public class OpenApiConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属应用ID
     */
    private Long appId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置UUID（用于生成被动模式接口路径）
     */
    private String configUuid;

    /**
     * 调用类型：1-被动（提供接口） 2-主动（调用外部）
     */
    private Integer callType;

    /**
     * 数据类型：user-用户 role-角色 dept-部门
     */
    private String dataType;

    /**
     * 同步方式：1-全量 2-增量
     */
    private Integer syncMode;

    /**
     * 接口路径（被动模式）
     */
    private String apiPath;

    /**
     * 请求方法：GET/POST/PUT/DELETE
     */
    private String apiMethod;

    /**
     * 远程接口地址（主动模式）
     */
    private String remoteUrl;

    /**
     * 远程请求方法
     */
    private String remoteMethod;

    /**
     * 请求头配置（JSON格式）
     */
    private String requestHeaders;

    /**
     * 请求参数模板（JSON格式）
     */
    private String requestParams;

    /**
     * 响应字段映射（JSON格式）
     */
    private String responseMapping;

    /**
     * 增量字段（如update_time）
     */
    private String incrementField;

    /**
     * 上次同步时间
     */
    private LocalDateTime lastSyncTime;

    /**
     * 上次同步值
     */
    private String lastSyncValue;

    /**
     * Cron表达式（主动定时同步）
     */
    private String cronExpression;

    /**
     * 是否自动同步：0-否 1-是
     */
    private Integer autoSync;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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
     * 关联的应用名称（非数据库字段）
     */
    @TableField(exist = false)
    private String appName;
}
