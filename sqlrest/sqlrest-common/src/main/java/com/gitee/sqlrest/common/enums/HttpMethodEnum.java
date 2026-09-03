package com.gitee.sqlrest.common.enums;

public enum HttpMethodEnum {
  GET, HEAD, PUT, POST, DELETE,
  ;

  public static boolean exists(String method) {
    for (HttpMethodEnum methodEnum : values()) {
      if (methodEnum.name().equals(method)) {
        return true;
      }
    }
    return false;
  }
}
