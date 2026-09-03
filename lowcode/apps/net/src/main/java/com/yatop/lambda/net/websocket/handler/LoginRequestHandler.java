package com.yatop.lambda.net.websocket.handler;


import com.yatop.lambda.net.websocket.protocol.packet.LoginRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.LoginResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录请求逻辑处理器
 * 保留
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();
    private static Logger log = LoggerFactory.getLogger(LoginRequestHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket req) throws Exception {
        Tio.joinChannel(ctx.channel(), req.getUserId(), req.getGroupIds());
        Tio.send(ctx, req, new LoginResponsePacket());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }


}
