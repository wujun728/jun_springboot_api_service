package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用实体类
 */
@Data
@TableName("sys_application")
public class Application {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用标识
     */
    private String appKey;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 应用描述
     */
    private String description;

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