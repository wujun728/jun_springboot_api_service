package com.yatop.lambda.net.websocket.session;

import com.yatop.lambda.net.websocket.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Groups {
    private static Logger log = LoggerFactory.getLogger(Groups.class);
    private static ConcurrentHashMap<Long, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();
    private static Groups instance;

    public static Groups getInstance() {
        if (null == instance) {
            instance = new Groups();
        }
        return instance;
    }

    public Groups() {
    }

    public List<Long> getGroupNames() {
        List<Long> groupNames = new ArrayList<>();
        for (Long groupid : channelGroupMap.keySet()) {
            groupNames.add(groupid);
        }
        return groupNames;
    }

    public void sendToOthers(Packet packet, Set<String> groups, Channel channel) {
        Iterator<String> its = groups.iterator();
        while (its.hasNext()) {
            ChannelGroup group = channelGroupMap.get(its.next());
            if (null == group) {
                return;
            }
            group.remove(channel);
            ChannelGroupFuture future = group.writeAndFlush(packet);
            future.addListener(f -> group.add(channel));
        }
    }

    public void sendToAll(Packet packet, Set<String> groups) {
        Iterator<String> its = groups.iterator();
        while (its.hasNext()) {
            ChannelGroup group = channelGroupMap.get(its.next());
            if (null == group) {
                return;
            }
            ChannelGroupFuture future = group.writeAndFlush(packet);
            future.addListener(f ->
            {
            });
        }
    }

    public void sendToGroup(Long groupId, Packet packet) {
        ChannelGroup group = channelGroupMap.get(groupId);
        if (null == group) {
            return;
        }
        ChannelGroupFuture future = group.writeAndFlush(packet);
        future.addListener(f ->
        {
        });
    }

    public void shutdownGracefully() {
        Iterator<ChannelGroup> groupIterator = channelGroupMap.values().iterator();
        while (groupIterator.hasNext()) {
            ChannelGroup group = groupIterator.next();
            group.close();
        }

    }

    public Set<LocalSession> getChannelContexts(Long groupid) {
        ChannelGroup group = channelGroupMap.get(groupid);
        if (null == group) {
            return null;
        }
        Set<LocalSession> set = new HashSet<>();
        Iterator<Channel> it = group.iterator();
        while (it.hasNext()) {
            Channel next = it.next();
            set.add(LocalSession.get(next));
        }
        return set;

    }

    public ChannelGroup getChannerGroup(Long groupId) {
        ChannelGroup group = channelGroupMap.get(groupId);
        if (null == group) {
            return null;
        }
        return group;
    }


    public LocalSession removeAll(LocalSession s, Set<Long> groups) {
        Iterator<Long> its = groups.iterator();
        while (its.hasNext()) {
            ChannelGroup group = channelGroupMap.get(its.next());
            if (null != group) {
                group.remove(s.getChannel());
            }
        }
        return s;
    }


    public void bind(LocalSession localSession) {
        if (localSession == null) {
            return;
        }
        Iterator<Long> its = localSession.getGroupIds().iterator();
        while (its.hasNext()) {
            bind(its.next(), localSession);
        }
    }

    public void bind(Long groupid, LocalSession channelContext) {
        try {
            ChannelGroup channelGroup = getChannerGroup(groupid);
            if (null == channelGroup) {
                channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                channelGroupMap.put(groupid, channelGroup);
            }
            Set<LocalSession> sessions = getChannelContexts(groupid);
            if (!sessions.contains(channelContext)) {
                channelGroup.add(channelContext.getChannel());
            }
        } catch (Exception var19) {
            log.error(var19.toString(), var19);
        }

    }

    public void unbind(Long groupid, LocalSession localSession) {
        try {
            ChannelGroup channelGroup = channelGroupMap.get(groupid);
            if (null != channelGroup) {
                channelGroup.remove(localSession.getChannel());
            }
        } catch (Throwable var20) {
            log.error(var20.toString(), var20);
        }
    }
}
