package com.yatop.lambda.net.websocket.session;

/**
 * create by mm
 **/
public interface ServerSession {
    void writeAndFlush(Object pkg);

    String getSessionId();

    boolean isValid();

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    Long getUserId();
}

