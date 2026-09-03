package com.yuyaogc.lowcode.engine.exception;

/**
 * 解析问题
 *
 *
 */
public class JsonRpcParseException extends JsonRpcException {

    public JsonRpcParseException() {
    }

    public JsonRpcParseException(Throwable cause) {
        super(cause);
    }

    public JsonRpcParseException(String message) {
        super(message);
    }

    public JsonRpcParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
