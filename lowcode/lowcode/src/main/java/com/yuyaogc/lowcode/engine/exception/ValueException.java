package com.yuyaogc.lowcode.engine.exception;

public class ValueException extends PlatformException {

    public ValueException(Throwable cause) {
        super(cause);
    }

    public ValueException(String message) {
        super(message);
    }

    public ValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueException(int errorCode) {
        super(errorCode);
    }

    public ValueException(Throwable cause, int errorCode) {
        super(cause, errorCode);
    }

    public ValueException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ValueException(String message, Throwable cause, int errorCode) {
        super(message, cause, errorCode);
    }
}