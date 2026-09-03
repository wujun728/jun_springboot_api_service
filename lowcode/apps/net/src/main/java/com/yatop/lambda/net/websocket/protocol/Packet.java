package com.yatop.lambda.net.websocket.protocol;

import lombok.Data;

import java.util.Set;

/**
 * @author mm
 * @date 2019-04-20
 */
@Data
public abstract class Packet {
    private Byte version = 1;

    private Long userId;
    private Set<Long> groupIds;



    private boolean success;

    private String errorMsg;

    public abstract Byte getCommand();

}
