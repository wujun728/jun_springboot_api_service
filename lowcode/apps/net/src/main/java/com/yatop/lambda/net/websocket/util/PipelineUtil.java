package com.yatop.lambda.net.websocket.util;


import com.yatop.lambda.net.websocket.handler.*;
import io.netty.channel.ChannelPipeline;

/**
 * ChannelPipeline工具类
 *
 * @author mm
 * @date 2019-04-22
 */
public class PipelineUtil {

    /**
     * 添加websocket/tcp通用handler
     *
     * @param pipeline
     */
    public static void addHandler(ChannelPipeline pipeline) {
        pipeline.addLast(
                LoginRequestHandler.INSTANCE,
                HeartBeatRequestHandler.INSTANCE,
                MsgSendRequestHandler.INSTANCE,
                LogoutRequestHandler.INSTANCE,
                JoinGroupRequestHandler.INSTANCE,
                QuitGroupRequestHandler.INSTANCE
        );
    }

}
