package com.gitee.sqlrest.common.enums;

import lombok.Getter;

@Getter
public enum ExpireTimeEnum {
  EXPIRE_FOR_EVER(DurationTimeEnum.FOR_EVER, -1l, "无期"),
  EXPIRE_ONLY_ONCE(DurationTimeEnum.ONLY_ONCE, 0l, "一次"),
  EXPIRE_05_MIN(DurationTimeEnum.TIME_VALUE, 5 * 60l, "5分钟"),
  EXPIRE_30_MIN(DurationTimeEnum.TIME_VALUE, 30 * 60l, "30分钟"),
  EXPIRE_01_HOUR(DurationTimeEnum.TIME_VALUE, 1 * 3600l, "1小时"),
  EXPIRE_12_HOUR(DurationTimeEnum.TIME_VALUE, 12 * 3600l, "12小时"),
  EXPIRE_01_DAY(DurationTimeEnum.TIME_VALUE, 24 * 3600l, "1天"),
  EXPIRE_15_DAY(DurationTimeEnum.TIME_VALUE, 15 * 24 * 3600l, "15天"),
  EXPIRE_01_MOUTH(DurationTimeEnum.TIME_VALUE, 30 * 24 * 3600l, "1个月"),

  EXPIRE_UNKNOWN(DurationTimeEnum.TIME_VALUE, Long.MAX_VALUE, "未知"),
  ;

  private DurationTimeEnum duration;
  private long value;
  private String description;

  ExpireTimeEnum(DurationTimeEnum duration, long value, String description) {
    this.duration = duration;
    this.value = value;
    this.description = description;
  }

  public static ExpireTimeEnum from(DurationTimeEnum duration, long expireAt) {
    for (ExpireTimeEnum expireTimeEnum : values()) {
      if (expireTimeEnum.getDuration().equals(duration)) {
        if (expireAt == expireTimeEnum.getValue()) {
          return expireTimeEnum;
        }
      }
    }
    return EXPIRE_UNKNOWN;
  }
}
