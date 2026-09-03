package com.yatop.lambda.net.websocket.protocol.packet;

import com.yatop.lambda.net.websocket.protocol.Packet;
import com.yatop.lambda.net.websocket.protocol.command.Command;
import lombok.Data;

@Data
public class QuitGroupRequestPacket extends Packet {
    private Long chatId;
    private int chatType;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }
}
