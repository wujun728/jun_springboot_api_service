package com.zapi.notification.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息实体类
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("sys_message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 消息链接
     */
    @TableField("url")
    private String messageUrl;

    /**
     * 消息图标
     */
    @TableField("icon")
    private String messageIcon;

    /**
     * 消息标签
     */
    @TableField("tag")
    private String messageTag;

    /**
     * 接收者目标（JSON 存储）
     */
    @TableField("receiver_targets")
    private String receiverTargetsJson;

    /**
     * 过期时间
     */
    @TableField(exist = false) // 当前表无此列，避免查询报错
    private LocalDateTime expireTime;

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

    /**
     * 接收者列表（前端多选时传递），不落库
     */
    @TableField(exist = false)
    private List<ReceiverTarget> receiverTargets;

    /**
     * 针对当前登录用户的阅读状态（从关联表映射）
     */
    @TableField(exist = false)
    private String receiverStatus;

    /**
     * 针对当前登录用户的阅读时间（从关联表映射）
     */
    @TableField(exist = false)
    private LocalDateTime receiverReadTime;
} 