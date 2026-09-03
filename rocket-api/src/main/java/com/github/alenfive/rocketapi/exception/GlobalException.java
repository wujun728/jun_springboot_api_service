package com.github.alenfive.rocketapi.exception;

import lombok.Data;

/**
 * @author liuchengbiao
 * @date 2020-06-12 16:11
 */
@Data
public class GlobalException extends RuntimeException {

    private int code = 500;

    public GlobalException(String msg) {
        super(msg);
    }

    public GlobalException(String msg, Throwable e) {
        super(msg, e);
    }

    public GlobalException(String msg, int code) {
        super(msg);
        this.setCode(code);
    }

    public GlobalException(String msg, int code, Throwable e) {
        super(msg, e);
        this.setCode(code);
    }
}
