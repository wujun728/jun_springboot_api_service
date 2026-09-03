package com.yatop.lambda.net.websocket.codec;


import com.yatop.lambda.net.websocket.protocol.Packet;
import com.yatop.lambda.net.websocket.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * Tcp数据包编解码器
 *
 * @author mm
 * @date 2019-04-21
 */
@ChannelHandler.Sharable
public class TcpPacketCodec extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final TcpPacketCodec INSTANCE = new TcpPacketCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodeC.INSTANCE.encode(byteBuf, msg);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(msg));
    }
}
