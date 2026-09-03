package com.gitee.sqlrest.common.enums;

import cn.hutool.core.date.DatePattern;

public enum DataTypeFormatEnum {
  DATE(java.sql.Date.class.getName(), DatePattern.NORM_DATE_PATTERN),
  LOCAL_DATE(java.time.LocalDate.class.getName(), DatePattern.NORM_DATE_PATTERN),

  TIME(java.sql.Time.class.getName(), DatePattern.NORM_TIME_PATTERN),

  LOCAL_DATE_TIME(java.time.LocalDateTime.class.getName(), DatePattern.NORM_DATETIME_PATTERN),
  TIMESTAMP(java.sql.Timestamp.class.getName(), DatePattern.NORM_DATETIME_PATTERN),

  BIG_DECIMAL(java.math.BigDecimal.class.getName(), 6),
  ;

  private String className;
  private String defaultPattern;
  private int numberScale;

  DataTypeFormatEnum(String className, String defaultPattern) {
    this.className = className;
    this.defaultPattern = defaultPattern;
  }

  DataTypeFormatEnum(String className, int numberScale) {
    this.className = className;
    this.numberScale = numberScale;
  }

  public String getClassName() {
    return className;
  }

  public String getDefaultPattern() {
    return defaultPattern;
  }

  public int getNumberScale() {
    return numberScale;
  }

  public String getDefault() {
    if (numberScale > 0) {
      return String.valueOf(numberScale);
    }
    return defaultPattern;
  }
}
