package com.zapi.util;

import java.util.UUID;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 生成UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带横线的UUID
     */
    public static String generateUUIDWithHyphen() {
        return UUID.randomUUID().toString();
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderscore(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     */
    public static String underscoreToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    sb.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 截取字符串
     */
    public static String substring(String str, int start, int end) {
        if (isEmpty(str)) {
            return str;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }

    /**
     * 左填充
     */
    public static String leftPad(String str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = length - str.length();
        if (pads <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pads; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 右填充
     */
    public static String rightPad(String str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = length - str.length();
        if (pads <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < pads; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
} 