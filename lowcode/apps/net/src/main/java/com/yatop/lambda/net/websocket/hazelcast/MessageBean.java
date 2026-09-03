package com.yatop.lambda.net.websocket.hazelcast;

import com.yatop.lambda.net.websocket.protocol.Packet;
import lombok.Data;

@Data
public class MessageBean {

    Long userId;
    Long groupId;
    Long toUserId;
    Packet packet;
}