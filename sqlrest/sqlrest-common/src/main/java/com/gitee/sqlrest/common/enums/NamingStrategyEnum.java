package com.gitee.sqlrest.common.enums;

import cn.hutool.core.util.StrUtil;
import java.util.function.Function;

public enum NamingStrategyEnum {
  NONE(Function.identity(), "无转换"),
  CAMEL_CASE(StrUtil::toCamelCase, "属性名转换为驼峰命名"),
  SNAKE_CASE(StrUtil::toUnderlineCase, "属性名转换为蛇形命名"),
  LOWER_CASE(String::toLowerCase, "属性名转换为小写字母"),
  UPPER_CASE(String::toUpperCase, "属性名转换为大写字母"),
  ;
  private Function<String, String> function;
  private String description;

  NamingStrategyEnum(Function<String, String> function, String description) {
    this.function = function;
    this.description = description;
  }

  public Function<String, String> getFunction() {
    return function;
  }

  public String getDescription() {
    return description;
  }
}
