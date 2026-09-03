package com.yatop.lambda.net.websocket.handler;

import com.yatop.lambda.net.websocket.session.LocalSession;
import com.yatop.lambda.net.websocket.session.Tio;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 空闲检查
 * 保留
 */

public class IMIdleStateHandler extends IdleStateHandler {
    private static final int READER_IDLE_TIME = 1000 * 5;
    private static Logger log = LoggerFactory.getLogger(IMIdleStateHandler.class);

    public IMIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        LocalSession channelContext = LocalSession.get(ctx.channel());
        Tio.unbind(ctx.channel(), channelContext.getUserId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }


}
