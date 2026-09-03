package com.gitee.sqlrest.core.serdes.datetime;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

public class LocalDateTimeValueSerializer extends StdSerializer<LocalDateTime> {

  private static final String DEFAULT_PATTERN = DatePattern.NORM_DATETIME_PATTERN;

  private String pattern;

  public LocalDateTimeValueSerializer(String pattern) {
    super(LocalDateTime.class);
    this.pattern = StringUtils.defaultIfBlank(pattern, DEFAULT_PATTERN);
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (value != null) {
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      jsonGenerator.writeString(sdf.format(value));
    }
  }
}
