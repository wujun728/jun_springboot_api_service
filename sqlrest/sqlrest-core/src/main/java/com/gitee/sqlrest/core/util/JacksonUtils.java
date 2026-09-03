package com.gitee.sqlrest.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gitee.sqlrest.common.enums.DataTypeFormatEnum;
import com.gitee.sqlrest.core.serdes.DateTimeSerDesFactory;
import java.util.Collections;
import java.util.Map;

public final class JacksonUtils {

  public static String toJsonStr(Object obj) {
    return toJsonStr(obj, Collections.emptyMap());
  }

  public static String toJsonStr(Object obj, Map<DataTypeFormatEnum, String> formatMap) {
    // https://www.jianshu.com/p/1368547350c6
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(createSerializeModule(formatMap));
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private static Module createSerializeModule(Map<DataTypeFormatEnum, String> formatMap) {
    Map<DataTypeFormatEnum, String> finalFormatMap = (null == formatMap) ? Collections.emptyMap() : formatMap;
    SimpleModule module = new SimpleModule();
    DateTimeSerDesFactory.getAllSerDesMap()
        .forEach(
            (clazz, creator) -> {
              StdSerializer serializer = creator.apply(finalFormatMap.get(clazz));
              module.addSerializer(serializer.handledType(), serializer);
            }
        );
    return module;
  }

}
