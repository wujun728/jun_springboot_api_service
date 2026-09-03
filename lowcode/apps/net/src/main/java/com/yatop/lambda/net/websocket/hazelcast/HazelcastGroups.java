package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.map.IMap;
import com.yatop.lambda.net.websocket.protocol.Packet;
import com.yatop.lambda.net.websocket.session.LocalSession;
import com.yatop.lambda.net.websocket.session.Users;

import java.util.Iterator;

public class HazelcastGroups {
    public static final String Group_ID = "Group_ID_";


    public void sendToGroup(Long groupId, Packet packet) {
        IMap<String, String> groupUsers = Hazelcast.getInstance().getMap(Group_ID + groupId);
//        Iterator<Cache.Entry<String, String>> iterator = groupUsers.iterator();
//        iterator.forEachRemaining((key) -> {
//            LocalSession local = Users.getInstance().get(key.getKey() + "");
//            if (null != local) {
//                log.info("receviceGroup : {}", local);
//                Users.getInstance().send(local.getChannel(), packet);
//            }
//        });
        HazelcastMq.getInstance().sendToGroup(groupId, packet.getUserId(), packet);
    }

    public void bind(String groupId, String userId) {
        IMap<String, String> groupUsers = Hazelcast.getInstance().getMap(Group_ID + groupId);
        groupUsers.putAsync(groupId, userId);
    }

    public void unbind(String groupId, String userId) {
        IMap<String, String> groupUsers = Hazelcast.getInstance().getMap(Group_ID + groupId);
        groupUsers.removeAsync(groupId);
    }

}
