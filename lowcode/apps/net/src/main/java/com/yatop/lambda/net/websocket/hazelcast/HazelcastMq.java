package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.topic.ITopic;
import com.yatop.lambda.net.websocket.protocol.Packet;

public class HazelcastMq {

    public static final String userid_topc = "userId_topc";
    public static final String group_topc = "group_topc";

    private static  HazelcastMq instance;

    public static HazelcastMq getInstance(){
        if(instance == null){
            instance = new HazelcastMq();
        }
        return instance;
    }

    private void receviceUser() {
        //发布/订阅模式
        ITopic<MessageBean> topic = Hazelcast.getInstance().getTopic(userid_topc);
        // 新增topic的监听者，具体实现加后文
        topic.addMessageListener(new UserTopicListener());
    }

    private void receviceGroup() {
        //发布/订阅模式
        ITopic<MessageBean> topic = Hazelcast.getInstance().getTopic(group_topc);
        // 新增topic的监听者，具体实现加后文
        topic.addMessageListener(new UserGroupTopicListener());
    }

    public void send2B(Long toUserId, Packet packet) {
        MessageBean messageBean = new MessageBean();
        messageBean.setPacket(packet);
        messageBean.setToUserId(toUserId);
        Hazelcast.getInstance().getTopic(userid_topc).publishAsync(messageBean);
    }


    public void sendToGroup(Long groupId, Long userId, Packet packet) {
        MessageBean messageBean = new MessageBean();
        messageBean.setPacket(packet);
        messageBean.setUserId(userId);
        messageBean.setGroupId(groupId);
        Hazelcast.getInstance().getTopic(group_topc).publishAsync(messageBean);
    }

}
