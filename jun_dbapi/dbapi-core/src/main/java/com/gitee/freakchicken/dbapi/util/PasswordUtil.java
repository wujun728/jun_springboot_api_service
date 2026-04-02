package com.gitee.freakchicken.dbapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密码工具类，替代原有的 MD5 明文密码方案。
 * 支持旧密码（明文）自动检测和迁移。
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final String BCRYPT_PREFIX = "$2a$";

    /**
     * 使用 BCrypt 哈希密码
     */
    public static String hashPassword(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配。
     * 支持两种模式：
     * 1. BCrypt 哈希密码（以 $2a$ 开头）
     * 2. 旧版明文密码（直接比较）
     */
    public static boolean checkPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        if (isBCryptHash(storedPassword)) {
            return ENCODER.matches(rawPassword, storedPassword);
        }
        // 兼容旧版明文密码
        return storedPassword.equals(rawPassword);
    }

    /**
     * 判断存储的密码是否已经是 BCrypt 格式
     */
    public static boolean isBCryptHash(String password) {
        return password != null && password.startsWith(BCRYPT_PREFIX);
    }
}
