package com.yatop.lambda.net.websocket.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推送
 */
public enum PushEnum {
    MESSAGE_SEND(1, "发送消息"),
    ADD_FRIEND(2, "增加好友"),
    JOIN_GROUP(3, "入群"),
    QUIT_GROUP(4, "退群"),
    DEL_GROUP_MEMBER(5, "移除成员"),
    LOGIN(6, "登录");


    private int code;
    private String msg;
    private static Map<Integer, PushEnum> CACHE_MAP = new ConcurrentHashMap();

    PushEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    static {
        PushEnum[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            PushEnum var3 = var0[var2];
            CACHE_MAP.put(var3.getCode(), var3);
        }
    }

    public static PushEnum valueOf(int value) {
        return CACHE_MAP.getOrDefault(value, MESSAGE_SEND);
    }
}
