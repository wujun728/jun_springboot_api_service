package com.gitee.sqlrest.core.util;

import com.gitee.sqlrest.common.consts.Constants;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ApiPathUtils {

  public static String getFullPath(String path) {
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    return String.format("/%s/%s", Constants.API_PATH_PREFIX, path);
  }
}
