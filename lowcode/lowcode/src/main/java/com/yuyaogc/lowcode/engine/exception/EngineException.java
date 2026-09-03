package com.yuyaogc.lowcode.engine.exception;

import com.yuyaogc.lowcode.engine.util.ThrowableUtils;

public class EngineException extends RuntimeException {

  private static final long serialVersionUID = 5224696788505678598L;

  public EngineException() {
    super();
  }

  public EngineException(String message) {
    super(message);
  }

  public EngineException(String message, Throwable cause) {
    super(message, cause);
  }

  public EngineException(EngineErrorEnum errorEnum, Throwable e){
    super(ThrowableUtils.getDebug(e));
  }

  public EngineException(EngineErrorEnum message) {
    super(message.getMsg());
  }

  public EngineException(Throwable cause) {
    super(cause);
  }

}
