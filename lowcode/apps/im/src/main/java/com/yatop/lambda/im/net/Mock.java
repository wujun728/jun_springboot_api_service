package com.yatop.lambda.im.net;


import com.yatop.lambda.im.constant.StatusCode;
import com.yatop.lambda.im.model.Conversation;
import com.yatop.lambda.im.net.packet.MessageSendRequestPacket;

public class Mock {


    public static Packet createGroupMessage(Conversation conversation) {
        MessageSendRequestPacket messageSendRequestPacket = new MessageSendRequestPacket();
        messageSendRequestPacket.setUserId(conversation.getUserId());
        messageSendRequestPacket.setConversationId(conversation.getConversationId());
        messageSendRequestPacket.setConversationType(conversation.getType());
        messageSendRequestPacket.setMessageId(conversation.getLastMessageId());
        messageSendRequestPacket.setBody(conversation.getLastMessage());
        messageSendRequestPacket.setTo(conversation.getConversationId());
        messageSendRequestPacket.setDirection("out");
        messageSendRequestPacket.setCode(StatusCode.CONVERSATION_CHANGED.getCode());
        return messageSendRequestPacket;
    }


}