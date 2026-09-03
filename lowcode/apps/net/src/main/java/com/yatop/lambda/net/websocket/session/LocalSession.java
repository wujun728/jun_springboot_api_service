package com.yatop.lambda.net.websocket.session;

import com.yatop.lambda.net.websocket.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Data
public class LocalSession implements ServerSession {
    private static Logger log = LoggerFactory.getLogger(LocalSession.class);
    public static final AttributeKey<LocalSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");
    public static final AttributeKey<String> KEY_USER_ID = AttributeKey.valueOf("key_user_id");

    private Channel channel;
    private Map<String, Object> map = new ConcurrentHashMap<>();
    private final String sessionId;
    private boolean isLogin = false;
    private Long userId;
    private Set<Long> groupIds;


    public LocalSession(Channel channel, Long userId, Set<Long> groupIds) {
        this.sessionId = buildNewSessionId();
        this.channel = channel;
        this.userId = userId;
        this.groupIds = groupIds;
        isLogin = true;
        channel.attr(KEY_USER_ID).set(JsonUtil.pojoToJson(userId));
        channel.attr(SESSION_KEY).set(this);
    }


    public LocalSession unbind() {
        isLogin = false;
        this.close();
        return this;
    }

    @Override
    public boolean isValid() {
        return getUserId() != null ? true : false;
    }


    private static String buildNewSessionId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }


    @Override
    public synchronized void writeAndFlush(Object pkg) {
        //当系统水位过高时，系统应不继续发送消息，防止发送队列积压
        //写Protobuf数据帧
        if (channel.isWritable()) //低水位
        {
            channel.writeAndFlush(pkg);
        } else {   //高水位时
            log.debug("通道很忙，消息被暂存了");
            //写入消息暂存的分布式存储，如果mongo
            //等channel空闲之后，再写出去
        }
    }

    //写Protobuf数据帧
    public synchronized void writeAndClose(Object pkg) {
        channel.writeAndFlush(pkg);
        close();
    }

    //关闭连接
    public synchronized void close() {
        //用户下线 通知其他节点
        ChannelFuture future = channel.close();
        future.addListener((ChannelFutureListener) future1 -> {
            if (!future1.isSuccess()) {
                log.error("CHANNEL_CLOSED error ");
            }
        });
    }

    public Long getUserId() {
        return userId;
    }

    public LocalSession setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */

    public static LocalSession get(Channel channel) {
        return channel.attr(LocalSession.SESSION_KEY).get();
    }

    public boolean equals(Channel fromChannel) {
        return fromChannel.id().equals(getChannel().id());
    }

    public synchronized void set(String key, Object value) {
        map.put(key, value);
    }

    public synchronized <T> T get(String key) {
        return (T) map.get(key);
    }
}
