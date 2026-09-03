package com.zapi.notification.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息模板实体类
 */
@Data
@TableName("sys_message_template")
public class MessageTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板标题
     */
    @TableField("title")
    private String templateTitle;

    /**
     * 模板内容
     */
    @TableField("content")
    private String templateContent;

    /**
     * 模板状态（ENABLED-启用，DISABLED-禁用）
     */
    @TableField("status")
    private String status;

    /**
     * 模板描述
     */
    private String description;

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