package com.zapi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 格式化当前时间
     */
    public static String formatNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    /**
     * 格式化指定时间
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    /**
     * 格式化指定时间
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * 格式化时间
     */
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 解析时间字符串
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    /**
     * 解析时间字符串
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 时间戳转LocalDateTime
     */
    public static LocalDateTime timestampToDateTime(long timestamp) {
        return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), java.time.ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转时间戳
     */
    public static long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
} 