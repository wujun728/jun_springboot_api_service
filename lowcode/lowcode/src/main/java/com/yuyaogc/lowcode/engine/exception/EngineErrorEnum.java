package com.yuyaogc.lowcode.engine.exception;

import java.net.Authenticator;

/**
 * 异常枚举
 */
public enum EngineErrorEnum {
    ActiveRecordException(100, "数据库异常"),
    JsonProcessingException(200, "序列化异常"),
    IllegalAccessException(300, "参数异常"),
    InvocationTargetException(400, "反射异常"),
    ModelException(500, "模型异常"),
    Authenticator(401, "认证异常"),
    UnauthorizedAccess(403, "无权访问");

    private Integer code;
    private String msg;

    EngineErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
