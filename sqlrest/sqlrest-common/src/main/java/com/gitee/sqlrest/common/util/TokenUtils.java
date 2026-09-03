package com.gitee.sqlrest.common.util;

import java.security.MessageDigest;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Token工具类
 */
@Slf4j
public final class TokenUtils {

  private static final char[] hexCode = "0123456789abcdefgh".toCharArray();

  public static String getRequestToken(HttpServletRequest httpRequest) {
    // 从header中获取token
    String authorization = httpRequest.getHeader("Authorization");
    if (!StringUtils.isEmpty(authorization)) {
      String[] splitString = authorization.split(" ");
      if (splitString.length == 2 && "Bearer".equalsIgnoreCase(splitString[0])) {
        return splitString[1];
      }
    }

    // 如果header中不存在token，则从参数中获取token
    if (StringUtils.isEmpty(authorization)) {
      return httpRequest.getParameter("token");
    }

    return null;
  }

  public static String generateValue() {
    return generateValue(UUID.randomUUID().toString());
  }

  protected static String generateValue(String param) {
    try {
      MessageDigest algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(param.getBytes());
      byte[] messageDigest = algorithm.digest();
      StringBuilder r = new StringBuilder(messageDigest.length * 2);
      for (byte b : messageDigest) {
        r.append(hexCode[(b >> 4) & 0xF]);
        r.append(hexCode[(b & 0xF)]);
      }
      return r.toString();
    } catch (Exception e) {
      throw new RuntimeException("Generate Token String failed: " + e.toString());
    }
  }

}
