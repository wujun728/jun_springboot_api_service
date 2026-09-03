package com.gitee.sqlrest.common.enums;

import java.util.function.Function;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ParamTypeEnum {
  LONG("整型", "number", 0L, Long.class, (String str) -> StringUtils.isNotBlank(str) ? Long.valueOf(str) : str),
  DOUBLE("浮点型", "number", 0D, Double.class, (String str) -> StringUtils.isNotBlank(str) ? Double.valueOf(str) : str),
  STRING("字符串", "string", "", String.class, (String str) -> str),
  DATE("日期", "string", "", String.class, (String str) -> str),
  TIME("时间", "string", "", String.class, (String str) -> str),
  BOOLEAN("布尔", "string", "true", Boolean.class, (String str) -> Boolean.parseBoolean(str));

  private String name;
  private String jsType;
  private Object example;
  private Class clazz;
  private Function<String, Object> converter;

  ParamTypeEnum(String name, String jsType, Object example, Class clazz, Function<String, Object> converter) {
    this.name = name;
    this.jsType = jsType;
    this.example = example;
    this.clazz = clazz;
    this.converter = converter;
  }
}
