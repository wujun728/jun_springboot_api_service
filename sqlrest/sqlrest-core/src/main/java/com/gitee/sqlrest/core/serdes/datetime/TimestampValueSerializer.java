package com.gitee.sqlrest.core.serdes.datetime;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;

public class TimestampValueSerializer extends StdSerializer<Timestamp> {

  private static final String DEFAULT_PATTERN = DatePattern.NORM_DATETIME_PATTERN;

  private String pattern;

  public TimestampValueSerializer(String pattern) {
    super(Timestamp.class);
    this.pattern = StringUtils.defaultIfBlank(pattern, DEFAULT_PATTERN);
  }

  @Override
  public void serialize(Timestamp value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (value != null) {
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      jsonGenerator.writeString(sdf.format(value));
    }
  }
}
