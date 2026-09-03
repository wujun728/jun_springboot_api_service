package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(displayName = "好友消息", name = "im_message")
public class Message extends Model<Message> {

    @Id
    private Long id;

    @Column(label = "消息 id")
    private String messageId;

    /**
     * 消息所属用户
     */
    @Column(label = "用户id")
    private Long userId;


    /**
     * 消息所属会话ID
     */
    @Column(label = "会话 id")
    private Long conversationId;

    /**
     * 会话类型
     */
    @Column(label = "会话类型")
    private String conversationType;

    /**
     * 消息方向：in=接收 out等于发出
     */
    @Column(label = "消息方向")
    private String direction;

    /**
     * 消息发送方
     */
    @Column(label = "消息发送方")
    private String from;


    @Column(label = "来自用户")
    private Long fromUserInfo;

    /**
     * 消息接收方
     */
    @Column(label = "消息接收方")
    private String to;

    /**
     * 消息类型
     */
    @Column(label = "消息类型")
    private String type;

    /**
     * 消息内容
     */
    @Column(label = "消息内容")
    private String body;

    /**
     * 扩展的自定义数据(字符串类型)
     */
    @Column(label = "扩展的自定义数据")
    private String extra;

    /**
     * 对方是否已读
     */
    @Column(label = "对方是否已读")
    private Integer isRead;

    /**
     * 是否被撤回的消息
     */
    @Column(label = "是否被撤回的消息")
    private Integer isRevoke;

    /**
     * 是否被删除的消息
     */
    @Column(label = "是否被删除的消息")
    private Integer isDeleted;

    /**
     * 消息状态：
     * unSend(未发送)
     * success(发送成功)
     * fail(发送失败)
     */
    @Column(label = "消息状态")
    private String status;

    /**
     * 对方接收状态
     * 0 = 未接收
     * 1 = 已接收
     */
    @Column(label = "对方接收状态")
    private Integer receive;

    /**
     * 消息时间，毫秒级时间戳
     */
    @Column(label = "消息时间")
    private Long time;
}
