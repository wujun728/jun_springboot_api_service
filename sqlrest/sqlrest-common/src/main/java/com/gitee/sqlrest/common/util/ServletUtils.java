package com.gitee.sqlrest.common.util;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 获取Servlet服务器的HTTP参数相关工具类
 */
@Slf4j
public final class ServletUtils {

  public static String getDomain(HttpServletRequest request) {
    StringBuffer url = request.getRequestURL();
    return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
  }

  public static String getOrigin(HttpServletRequest request) {
    return request.getHeader("Origin");
  }

  public static String getPathUri(HttpServletRequest request) {
    return request.getRequestURI();
  }

  public static String getUserAgent(HttpServletRequest request) {
    return request.getHeader("user-agent");
  }

  /**
   * 获取IP地址
   * <p>
   * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * </p>
   * <p>
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中 第一个非unknown的有效IP字符串，则为真实IP地址.
   * </p>
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = "0.0.0.0";
    try {
      ip = request.getHeader("x-forwarded-for");
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
    } catch (Exception e) {
      log.error("get client IP address error: ", e);
    }

    return ip;
  }

}
