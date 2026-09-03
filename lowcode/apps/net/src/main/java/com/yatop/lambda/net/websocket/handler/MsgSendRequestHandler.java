package com.yatop.lambda.net.websocket.handler;


import cn.hutool.core.bean.BeanUtil;
import com.yatop.lambda.net.websocket.protocol.packet.MessageSendRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.MessageSendResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import com.yatop.lambda.net.websocket.session.Users;
import com.yuyaogc.lowcode.engine.util.IdWorker;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 好友发送通道
 * 保留
 */
@ChannelHandler.Sharable
@Slf4j
public class MsgSendRequestHandler extends SimpleChannelInboundHandler<MessageSendRequestPacket> {

    public static final MsgSendRequestHandler INSTANCE = new MsgSendRequestHandler();
    protected final int GROUP = 1;
    protected final int FRIEND = 0;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageSendRequestPacket req) throws Exception {
        MessageSendResponsePacket res = new MessageSendResponsePacket();
        BeanUtil.copyProperties(req, res);
        res.setSuccess(true);
        res.setMessageId(IdWorker.getId());

        switch (req.getConversationType()) {
            case "private":
                Tio.send2B(req.getTo(), res);
                Tio.send(ctx, req, res);
                break;
            case "group":
                Tio.sendToGroup(req.getTo(), req, res);
                break;
            default: {
            }
        }
        log.info("{}", res);
    }
}
