package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import com.yatop.lambda.net.websocket.session.LocalSession;
import com.yatop.lambda.net.websocket.session.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTopicListener implements MessageListener<MessageBean> {
    private static Logger log = LoggerFactory.getLogger(UserTopicListener.class);
    @Override
    public void onMessage(Message<MessageBean> message) {
        MessageBean m = message.getMessageObject();
        LocalSession local = Users.getInstance().get(m.getToUserId());
        if (null != local) {
            log.info("receviceUser : {}", local);
            Users.getInstance().send(local.getChannel(), m.getPacket());
        }
    }
}