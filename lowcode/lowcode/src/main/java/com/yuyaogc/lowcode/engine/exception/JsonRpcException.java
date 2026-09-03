package com.yuyaogc.lowcode.engine.exception;

/**
 * json rpc问题
 *
 *
 */
public class JsonRpcException extends RuntimeException {

    public JsonRpcException() {
    }

    public JsonRpcException(Throwable cause) {
        super(cause);
    }

    public JsonRpcException(String message) {
        super(message);
    }

    public JsonRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
