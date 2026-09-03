package com.yatop.lambda.net.websocket.handler;

import com.yatop.lambda.net.websocket.protocol.packet.JoinGroupRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.JoinGroupResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket req) throws Exception {
        JoinGroupResponsePacket res = new JoinGroupResponsePacket();
        if (1 == req.getChatType()) {
            Tio.joinGroup(req.getChatId(), req.getUserId());
        }
        res.setSuccess(true);
        Tio.send(ctx, req, res);
    }
}
