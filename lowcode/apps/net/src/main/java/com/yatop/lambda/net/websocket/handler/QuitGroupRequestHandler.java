package com.yatop.lambda.net.websocket.handler;

import com.yatop.lambda.net.websocket.protocol.packet.QuitGroupRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.QuitGroupResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket req) throws Exception {

        QuitGroupResponsePacket res = new QuitGroupResponsePacket();
        res.setSuccess(true);

        if (req.getChatType() == 1) {
            Tio.quitGroup(req.getChatId(), req.getUserId());
        }

        Tio.send(ctx, req, res);
    }
}
