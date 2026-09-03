package com.gitee.sqlrest.common.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码工具类
 */
public final class PasswordUtils {

  public static String encryptPassword(String password, String credentialsSalt) {
    return BCrypt.hashpw(password, credentialsSalt);
  }

  public static void main(String[] args) {
    String password = "123456";
    String credentialsSalt = "$2a$10$eUanVjvzV27BBxAb4zuBCu";
    String newPassword = encryptPassword(password, credentialsSalt);
    System.out.println(newPassword);
    System.out.println(credentialsSalt);
  }

}
