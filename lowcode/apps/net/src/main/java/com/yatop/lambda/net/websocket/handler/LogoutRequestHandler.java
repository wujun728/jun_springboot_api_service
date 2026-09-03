package com.yatop.lambda.net.websocket.handler;


import com.yatop.lambda.net.websocket.protocol.packet.LogoutRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.LogoutResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 注销
 * 保留
 */
@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) throws Exception {
        LogoutResponsePacket res = new LogoutResponsePacket();
//        try {
//            res.setSuccess(true);
//            LocalSession channelContext = LocalSession.get(ctx.channel());
//            Tio.unbind(ctx.channel(), channelContext.getUserId());
//        } catch (Exception e) {
//            res.setSuccess(false);
//            res.setErrorMsg(e.getMessage());
//        }
        Tio.send(ctx, msg, res);
    }
}
