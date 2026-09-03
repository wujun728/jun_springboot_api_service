package com.yatop.lambda.im.net;

import lombok.Data;

/**
 * @author mm
 * @date 2019-04-20
 */
@Data
public abstract class Packet {
    private Byte version = 1;

    private Long userId;
    private Long[] groupIds;

    private boolean success;
    private String errorMsg;

    public abstract Byte getCommand();

}