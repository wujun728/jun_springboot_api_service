package com.yuyaogc.lowcode.engine.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.util.ThrowableUtils;

/**
 * json rpc 响应
 *
 *
 */
public class JsonRpcResponse {
    RpcId id;
    String msg;

    int state;

    @JsonInclude(Include.NON_NULL)
    Object data;

    public JsonRpcResponse() {
    }



    public JsonRpcResponse(Object data) {
        this.data = data;
        this.state = 200;
    }


    public JsonRpcResponse(EngineErrorEnum errorEnum, String msg){
        this.state = errorEnum.getCode();
        this.msg = String.format("%s  %s", errorEnum.getMsg() , msg);
    }

    public JsonRpcResponse(EngineErrorEnum errorEnum, Throwable e){
        this.state = errorEnum.getCode();
        this.msg = String.format("%s  %s", errorEnum.getMsg() , ThrowableUtils.getDebug(e));
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public RpcId getId() {
        return id;
    }

    public void setId(RpcId id) {
        this.id = id;
    }
}
