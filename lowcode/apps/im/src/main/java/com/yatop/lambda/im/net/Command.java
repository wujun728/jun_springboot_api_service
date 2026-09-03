package com.yatop.lambda.im.net;

/**
 * 指令
 *
 * @author mm
 * @date 2019-04-20
 */
public interface Command {
    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;
    Byte MESSAGE_RESPONSE = 4;

    Byte LOGOUT_REQUEST = 5;
    Byte LOGOUT_RESPONSE = 6;

    Byte HEART_BEAT_REQUEST = 17;
    Byte HEART_BEAT_RESPONSE = 18;

    Byte PUSH_RES = -10;
    Byte PUSH_REQUEST = -9;

    Byte JOIN_GROUP_REQUEST = 7;
    Byte JOIN_GROUP_RESPONSE8 = 8;

    Byte QUIT_GROUP_REQUEST = 9;
    Byte QUIT_GROUP_RESPONSE = 10;
}