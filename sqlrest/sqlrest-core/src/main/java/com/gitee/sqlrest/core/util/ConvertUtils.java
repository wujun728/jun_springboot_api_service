package com.gitee.sqlrest.core.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ConvertUtils {

  public static Map<String, Object> to(Map<String, Object> row) {
    return row;
  }

  public static Map<String, Object> to(Map<String, Object> row, Function<String, String> converter) {
    if (null == converter) {
      return row;
    }
    Map<String, Object> ret = new LinkedHashMap<>();
    row.forEach((key, val) -> ret.put(converter.apply(key), val));
    return ret;
  }

}
