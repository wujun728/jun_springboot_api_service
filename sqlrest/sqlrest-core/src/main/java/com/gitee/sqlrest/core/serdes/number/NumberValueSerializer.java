package com.gitee.sqlrest.core.serdes.number;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.math.NumberUtils;

public class NumberValueSerializer extends StdSerializer<Number> {

  private static final int DEFAULT_DECIMAL_SCALE = 6;

  private int decimalScale;

  public NumberValueSerializer(String numberScaleStr) {
    super(Number.class);
    this.decimalScale = NumberUtils.toInt(numberScaleStr, DEFAULT_DECIMAL_SCALE);
  }

  @Override
  public void serialize(Number value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (value != null) {
      if (value instanceof BigDecimal) {
        BigDecimal decimal = ((BigDecimal) value).setScale(this.decimalScale);
        jsonGenerator.writeNumber(decimal);
      } else if (value instanceof BigInteger) {
        jsonGenerator.writeNumber((BigInteger) value);
      } else if (value instanceof Long) {
        jsonGenerator.writeNumber(value.longValue());
      } else if (value instanceof Double) {
        jsonGenerator.writeNumber(value.doubleValue());
      } else if (value instanceof Float) {
        jsonGenerator.writeNumber(value.floatValue());
      } else if (!(value instanceof Integer) && !(value instanceof Byte) && !(value instanceof Short)) {
        jsonGenerator.writeNumber(value.toString());
      } else {
        jsonGenerator.writeNumber(value.intValue());
      }
    }
  }

}
