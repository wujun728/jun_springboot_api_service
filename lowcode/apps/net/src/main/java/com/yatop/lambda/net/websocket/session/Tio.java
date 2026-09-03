package com.yatop.lambda.net.websocket.session;

import com.yatop.lambda.net.websocket.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class Tio {


    /**
     * 加入通道
     *
     * @param channel  用户通道
     * @param userId   用户id
     * @param groupIds 群组ids
     * @throws Exception
     */
    public static void joinChannel(Channel channel, Long userId, Set<Long> groupIds) throws Exception {
        if (null != userId) {
            LocalSession localSession = Users.getInstance().get(userId);
            // 0.重新绑定通道
            if (null == localSession) {
                localSession = new LocalSession(channel, userId, groupIds);
                Users.getInstance().bind(userId, localSession);
                Groups.getInstance().bind(localSession);
            } else {
                // 当前用户已经存在
                if (localSession.getUserId().equals(userId)) {
                    // 后面登录的用户抢占通道
                    if (!localSession.getChannel().id().equals(channel.id())) {
                        // 2.建立新的通道绑定
                        localSession = new LocalSession(channel, userId, groupIds);
                        // 3. 用户通道
                        Users.getInstance().bind(userId, localSession);
                        // 4. 群通道
                        Groups.getInstance().bind(localSession);
                    }
                }
            }
        }
    }

    /**
     * 移除绑定
     *
     * @param channel
     * @param userid
     */
    public static void unbind(Channel channel, Long userid) {
        LocalSession localSession = Users.getInstance().get(userid);
        if (null == localSession) {
            return;
        }
        Users.getInstance().unbind(userid);
        Users.getInstance().closeChannel(localSession);
    }


    /**
     * 加入群组通道
     */
    public static void joinGroup(Long groupId, Long userId) {
        LocalSession localSession = Users.getInstance().get(userId);
        if (localSession == null) {
            return;
        }
        Groups.getInstance().bind(groupId, localSession);
    }

    public static void quitGroup(Long groupId, Long userId) {
        LocalSession localSession = Users.getInstance().get(userId);
        if (localSession == null) {
            return;
        }
        Groups.getInstance().unbind(groupId, localSession);
    }


    /**
     * 需要判断通道是否是自己并且绑定通道
     *
     * @param ctx
     * @param req
     * @param res
     */
    public static void send(ChannelHandlerContext ctx, Packet req, Packet res) throws Exception {
        Users.getInstance().send(ctx.channel(), res);
    }

    /**
     * 发送对方不需要重建通道绑定
     *
     * @param toUserId
     * @param packet
     */
    public static void send2B(Long toUserId, Packet packet) {
        LocalSession local = Users.getInstance().get(toUserId);
        if (null == local) {
            return;
        }
        Users.getInstance().send(local.getChannel(), packet);
    }

    /**
     * 需要判断自己是否在群组通道里面
     */
    public static void sendToGroup(Long groupId, Packet req, Packet res) {
        Groups.getInstance().sendToGroup(groupId, res);
    }

    /**
     * 给所有发送消息
     */
    public static void sendToAll(Packet res, ChannelHandlerContext ctx) {
        LocalSession local = LocalSession.get(ctx.channel());
        if (null == local) {
            return;
        }
        Users.getInstance().sendAll(res);
    }
}
