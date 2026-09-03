package com.yuyaogc.lowcode.engine.jsonrpc;


import com.yuyaogc.lowcode.engine.util.ThrowableUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * json rpc 错误
 *
 *
 */
public class JsonRpcError {
    int code;
    String message;
    Object data;

    public JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonRpcError(int code, String message, Throwable error) {
        this.code = code;
        this.message = message;
        if (error != null) {
            data = createData(error);
        }
    }

    Object createData(Throwable error) {
        Map<String, String> map = new HashMap<>(3);
        Throwable cause = ThrowableUtils.getCause(error);
        map.put("name", cause.getClass().getName());
        map.put("debug", ThrowableUtils.getDebug(error));
        return map;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static JsonRpcError createParseError(Throwable e) {
        return new JsonRpcError(-32700, "Parse error", e);
    }

    public static JsonRpcError createInvalidRequest(Throwable e) {
        return new JsonRpcError(-32600, "Invalid Request", e);
    }

    public static JsonRpcError createMethodNotFound(Throwable e) {
        return new JsonRpcError(-32601, "Method not found", e);
    }

    public static JsonRpcError createInvalidParams(Throwable e) {
        return new JsonRpcError(-32602, "Invalid params", e);
    }

    public static JsonRpcError createInternalError(Throwable e) {
        return new JsonRpcError(-32603, "Internal error", e);
    }
}
