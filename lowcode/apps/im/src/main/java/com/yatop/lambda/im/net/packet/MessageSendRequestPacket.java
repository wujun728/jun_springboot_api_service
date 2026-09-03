package com.yatop.lambda.im.net.packet;

import com.yatop.lambda.im.net.Command;
import com.yatop.lambda.im.net.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端发送至服务端的消息数据包
 *
 * @author mm
 * @date 2019-04-20
 */
@Data
@NoArgsConstructor
public class


MessageSendRequestPacket extends Packet {

    private Long sequence;

    /**
     * 消息ID
     * 私聊消息ID组成：分布式唯一ID-毫秒级时间戳-发送端1（接收端2）
     */
    private Long messageId;

    /**
     * 消息所属用户
     */
    private Long userId;


    /**
     * 消息所属会话ID
     */
    private Long conversationId;

    /**
     * 会话类型
     */
    private String conversationType;

    /**
     * 消息方向：in=接收 out等于发出
     */
    private String direction;

    /**
     * 消息发送方
     */
    private Long from;


    private String fromUserInfo;

    /**
     * 消息接收方
     */
    private Long to;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private Object body;

    /**
     * 扩展的自定义数据(字符串类型)
     */
    private String extra;

    /**
     * 对方是否已读
     */
    private Integer isRead;

    /**
     * 是否被撤回的消息
     */
    private Integer isRevoke;

    /**
     * 是否被删除的消息
     */
    private Integer isDeleted;

    /**
     * 消息状态：
     * unSend(未发送)
     * success(发送成功)
     * fail(发送失败)
     */
    private String status;

    /**
     * 对方接收状态
     * 0 = 未接收
     * 1 = 已接收
     */
    private Integer receive;

    /**
     * 消息时间，毫秒级时间戳
     */
    private Long time;

    /**
     * 编号
     */
    private int code;


    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
