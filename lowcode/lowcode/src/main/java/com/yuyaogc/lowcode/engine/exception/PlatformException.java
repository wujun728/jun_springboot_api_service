package com.yuyaogc.lowcode.engine.exception;

public class PlatformException extends RuntimeException {
    int errorCode = 1;

    public int getErrorCode() {
        return errorCode;
    }

    public PlatformException(Throwable cause) {
        super(cause);
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlatformException(int errorCode) {
        this.errorCode = errorCode;
    }

    public PlatformException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public PlatformException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PlatformException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}