package com.yuyaogc.lowcode.engine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeKit {
    private static final Map<String, DateTimeFormatter> formaters = new SyncWriteMap();
    private static final ThreadLocal<HashMap<String, SimpleDateFormat>> TL = ThreadLocal.withInitial(() -> {
        return new HashMap();
    });

    public TimeKit() {
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        DateTimeFormatter ret = (DateTimeFormatter) formaters.get(pattern);
        if (ret == null) {
            ret = DateTimeFormatter.ofPattern(pattern);
            formaters.put(pattern, ret);
        }

        return ret;
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat ret = (SimpleDateFormat) ((HashMap) TL.get()).get(pattern);
        if (ret == null) {
            ret = new SimpleDateFormat(pattern);
            ((HashMap) TL.get()).put(pattern, ret);
        }

        return ret;
    }

    public static String now(String pattern) {
        return LocalDateTime.now().format(getDateTimeFormatter(pattern));
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(getDateTimeFormatter(pattern));
    }

    public static String format(LocalDate localDate, String pattern) {
        return localDate.format(getDateTimeFormatter(pattern));
    }

    public static String format(LocalTime localTime, String pattern) {
        return localTime.format(getDateTimeFormatter(pattern));
    }

    public static String format(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String dateString, String pattern) {
        try {
            return getSimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static LocalDateTime parseLocalDateTime(String localDateTimeString, String pattern) {
        return LocalDateTime.parse(localDateTimeString, getDateTimeFormatter(pattern));
    }

    public static LocalDate parseLocalDate(String localDateString, String pattern) {
        return LocalDate.parse(localDateString, getDateTimeFormatter(pattern));
    }

    public static LocalTime parseLocalTime(String localTimeString, String pattern) {
        return LocalTime.parse(localTimeString, getDateTimeFormatter(pattern));
    }

    public static boolean isAfter(ChronoLocalDateTime<?> self, ChronoLocalDateTime<?> other) {
        return self.isAfter(other);
    }

    public static boolean isBefore(ChronoLocalDateTime<?> self, ChronoLocalDateTime<?> other) {
        return self.isBefore(other);
    }

    public static boolean isEqual(ChronoLocalDateTime<?> self, ChronoLocalDateTime<?> other) {
        return self.isEqual(other);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date instanceof java.sql.Date) {
            date = new Date(date.getTime());
        }

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            date = new Date(date.getTime());
        }

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static LocalTime toLocalTime(Date date) {
        if (date instanceof java.sql.Date) {
            date = new Date(date.getTime());
        }

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }
}
