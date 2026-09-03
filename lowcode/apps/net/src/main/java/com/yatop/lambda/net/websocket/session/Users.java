package com.yatop.lambda.net.websocket.session;

import com.yatop.lambda.net.websocket.protocol.Packet;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class Users {
    private ConcurrentHashMap<Long, LocalSession> channelContextMap = new ConcurrentHashMap<>();
    private static Logger log = LoggerFactory.getLogger(Users.class);
    private static Users instance;

    public static Users getInstance() {
        if (null == instance) {
            instance = new Users();
        }
        return instance;
    }

    public Users() {
    }


    /**
     * 绑定会话
     *
     * @param userid
     * @param localSession
     */
    public void bind(Long userid, LocalSession localSession) {
        try {
            channelContextMap.put(userid, localSession);
        } catch (Exception var19) {
            log.error(var19.toString(), var19);
        }

    }

    public void unbind(Long userid) {
        try {
            if (!channelContextMap.containsKey(userid)) {
                return;
            }
            channelContextMap.remove(userid);
        } catch (Throwable var20) {
            log.error(var20.toString(), var20);
        }
    }

    public void send(Channel channel, Packet msg) {
        LocalSession localSession = LocalSession.get(channel);
        if (localSession == null) {
            return;
        }
        localSession.writeAndFlush(msg);
    }

    public void sendAll(Packet packet) {
        channelContextMap.values().stream().forEach(c -> send(c.getChannel(), packet));
    }


    public LocalSession get(Long userid) {
        if (channelContextMap.containsKey(userid)) {
            return channelContextMap.get(userid);
        } else {
            return null;
        }
    }

    public void closeChannel(LocalSession localSession) {
        localSession.unbind();
    }
}
