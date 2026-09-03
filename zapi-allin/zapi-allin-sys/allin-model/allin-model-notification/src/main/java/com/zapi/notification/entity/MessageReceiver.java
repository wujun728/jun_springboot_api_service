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
 * 消息-接收人关联表
 */
@Data
@TableName("sys_message_receiver")
public class MessageReceiver {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 接收人用户ID（若按部门/角色未展开，可为空）
     */
    private Long receiverId;

    /**
     * 来源类型：user / dept / role
     */
    private String receiverType;

    /**
     * 来源目标ID（部门ID/角色ID/用户ID）
     */
    private Long receiverTargetId;

    /**
     * 状态：UNREAD / READ / DELETED
     */
    private String status;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}

