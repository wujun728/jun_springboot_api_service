package com.gitee.sqlrest.common.enums;

public enum ParamLocationEnum {
  REQUEST_HEADER("请求头"),
  REQUEST_BODY("请求体"),
  FORM_DATA("表单数据"),
  ;

  private String name;

  ParamLocationEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
