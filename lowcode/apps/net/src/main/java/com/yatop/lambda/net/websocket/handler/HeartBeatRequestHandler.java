package com.yatop.lambda.net.websocket.handler;


import com.yatop.lambda.net.websocket.protocol.packet.HeartBeatRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.HeartBeatResponsePacket;
import com.yatop.lambda.net.websocket.session.Users;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳包
 * 保留
 */
@ChannelHandler.Sharable
@Slf4j
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket req) throws Exception {
        Users.getInstance().send(ctx.channel(), new HeartBeatResponsePacket());
    }
}
