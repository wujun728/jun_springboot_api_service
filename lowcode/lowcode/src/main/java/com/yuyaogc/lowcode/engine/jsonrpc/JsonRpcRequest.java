package com.yuyaogc.lowcode.engine.jsonrpc;

/**
 * json rpc 请求
 *
 *
 */
public class JsonRpcRequest {
    RpcId id;
    String jsonrpc = "2.0";
    String method;
    JsonRpcParameter params;

    public RpcId getId() {
        return id;
    }

    public void setId(RpcId id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JsonRpcParameter getParams() {
        return params;
    }

    public void setParams(JsonRpcParameter params) {
        this.params = params;
    }
}
