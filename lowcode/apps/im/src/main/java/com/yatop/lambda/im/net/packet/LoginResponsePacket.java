package com.yatop.lambda.im.net.packet;

import lombok.Data;
import com.yatop.lambda.im.net.Command;
import com.yatop.lambda.im.net.Packet;
/**
 * 登录响应数据包
 *
 * @author mm
 * @date 2019-04-20
 */
@Data
public class LoginResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
