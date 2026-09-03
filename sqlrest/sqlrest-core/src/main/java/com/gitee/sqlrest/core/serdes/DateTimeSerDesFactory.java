package com.gitee.sqlrest.core.serdes;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gitee.sqlrest.common.enums.DataTypeFormatEnum;
import com.gitee.sqlrest.core.serdes.datetime.DateValueSerializer;
import com.gitee.sqlrest.core.serdes.datetime.LocalDateTimeValueSerializer;
import com.gitee.sqlrest.core.serdes.datetime.LocalDateValueSerializer;
import com.gitee.sqlrest.core.serdes.datetime.TimeValueSerializer;
import com.gitee.sqlrest.core.serdes.datetime.TimestampValueSerializer;
import com.gitee.sqlrest.core.serdes.number.NumberValueSerializer;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class DateTimeSerDesFactory {

  private static Map<DataTypeFormatEnum, Function<String, StdSerializer>> DATE_TIME_SER_MAP = new HashMap<>();

  static {
    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.DATE, DateValueSerializer::new);
    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.LOCAL_DATE, LocalDateValueSerializer::new);

    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.TIME, TimeValueSerializer::new);

    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.TIMESTAMP, TimestampValueSerializer::new);
    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.LOCAL_DATE_TIME, LocalDateTimeValueSerializer::new);

    DATE_TIME_SER_MAP.put(DataTypeFormatEnum.BIG_DECIMAL, NumberValueSerializer::new);
  }

  public static Map<DataTypeFormatEnum, Function<String, StdSerializer>> getAllSerDesMap() {
    return ImmutableMap.copyOf(DATE_TIME_SER_MAP);
  }
}
