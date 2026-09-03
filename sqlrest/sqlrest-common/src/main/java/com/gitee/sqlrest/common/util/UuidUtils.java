package com.gitee.sqlrest.common.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

/**
 * UUID工具类
 */
@UtilityClass
public final class UuidUtils {

  public static String generateUuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

}
