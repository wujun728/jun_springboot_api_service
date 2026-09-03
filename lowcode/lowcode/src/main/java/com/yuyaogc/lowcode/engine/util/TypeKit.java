package com.yuyaogc.lowcode.engine.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Date;

public class TypeKit {
    private static final String datePattern = "yyyy-MM-dd";
    private static final int dateLen = "yyyy-MM-dd".length();
    private static final String dateTimeWithoutSecondPattern = "yyyy-MM-dd HH:mm";
    private static final int dateTimeWithoutSecondLen = "yyyy-MM-dd HH:mm".length();
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public TypeKit() {
    }

    public static String toStr(Object s) {
        return s != null ? s.toString() : null;
    }

    public static Integer toInt(Object n) {
        if (n instanceof Integer) {
            return (Integer)n;
        } else if (n instanceof Number) {
            return ((Number)n).intValue();
        } else {
            return n != null ? Integer.parseInt(n.toString()) : null;
        }
    }

    public static Long toLong(Object n) {
        if (n instanceof Long) {
            return (Long)n;
        } else if (n instanceof Number) {
            return ((Number)n).longValue();
        } else {
            return n != null ? Long.parseLong(n.toString()) : null;
        }
    }

    public static Double toDouble(Object n) {
        if (n instanceof Double) {
            return (Double)n;
        } else if (n instanceof Number) {
            return ((Number)n).doubleValue();
        } else {
            return n != null ? Double.parseDouble(n.toString()) : null;
        }
    }

    public static BigDecimal toBigDecimal(Object n) {
        if (n instanceof BigDecimal) {
            return (BigDecimal)n;
        } else {
            return n != null ? new BigDecimal(n.toString()) : null;
        }
    }

    public static Float toFloat(Object n) {
        if (n instanceof Float) {
            return (Float)n;
        } else if (n instanceof Number) {
            return ((Number)n).floatValue();
        } else {
            return n != null ? Float.parseFloat(n.toString()) : null;
        }
    }

    public static Short toShort(Object n) {
        if (n instanceof Short) {
            return (Short)n;
        } else if (n instanceof Number) {
            return ((Number)n).shortValue();
        } else {
            return n != null ? Short.parseShort(n.toString()) : null;
        }
    }

    public static Byte toByte(Object n) {
        if (n instanceof Byte) {
            return (Byte)n;
        } else if (n instanceof Number) {
            return ((Number)n).byteValue();
        } else {
            return n != null ? Byte.parseByte(n.toString()) : null;
        }
    }

    public static Boolean toBoolean(Object b) {
        if (b instanceof Boolean) {
            return (Boolean)b;
        } else if (b == null) {
            return null;
        } else {
            String s = b.toString();
            if (!"true".equalsIgnoreCase(s) && !"1".equals(s)) {
                return !"false".equalsIgnoreCase(s) && !"0".equals(s) ? (Boolean)b : Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }
    }

    public static Number toNumber(Object n) {
        if (n instanceof Number) {
            return (Number)n;
        } else if (n == null) {
            return null;
        } else {
            String s = n.toString();
            return s.indexOf(46) != -1 ? Double.parseDouble(s) : (double)Long.parseLong(s);
        }
    }

    public static Date toDate(Object d) {
        if (d instanceof Date) {
            return (Date)d;
        } else {
            if (d instanceof Temporal) {
                if (d instanceof LocalDateTime) {
                    return TimeKit.toDate((LocalDateTime)d);
                }

                if (d instanceof LocalDate) {
                    return TimeKit.toDate((LocalDate)d);
                }

                if (d instanceof LocalTime) {
                    return TimeKit.toDate((LocalTime)d);
                }
            }

            if (d instanceof String) {
                String s = (String)d;
                if (s.length() <= dateLen) {
                    return TimeKit.parse(s, "yyyy-MM-dd");
                }

                if (s.length() > dateTimeWithoutSecondLen) {
                    return TimeKit.parse(s, "yyyy-MM-dd HH:mm:ss");
                }

                int index = s.indexOf(58);
                if (index != -1) {
                    if (index != s.lastIndexOf(58)) {
                        return TimeKit.parse(s, "yyyy-MM-dd HH:mm:ss");
                    }

                    return TimeKit.parse(s, "yyyy-MM-dd HH:mm");
                }
            }

            return (Date)d;
        }
    }

    public static LocalDateTime toLocalDateTime(Object ldt) {
        if (ldt instanceof LocalDateTime) {
            return (LocalDateTime)ldt;
        } else if (ldt instanceof LocalDate) {
            return ((LocalDate)ldt).atStartOfDay();
        } else if (ldt instanceof LocalTime) {
            return LocalDateTime.of(LocalDate.now(), (LocalTime)ldt);
        } else if (ldt instanceof Date) {
            return TimeKit.toLocalDateTime((Date)ldt);
        } else {
            if (ldt instanceof String) {
                String s = (String)ldt;
                if (s.length() <= dateLen) {
                    return TimeKit.parseLocalDateTime(s, "yyyy-MM-dd");
                }

                if (s.length() > dateTimeWithoutSecondLen) {
                    return TimeKit.parseLocalDateTime(s, "yyyy-MM-dd HH:mm:ss");
                }

                int index = s.indexOf(58);
                if (index != -1) {
                    if (index != s.lastIndexOf(58)) {
                        return TimeKit.parseLocalDateTime(s, "yyyy-MM-dd HH:mm:ss");
                    }

                    return TimeKit.parseLocalDateTime(s, "yyyy-MM-dd HH:mm");
                }
            }

            return (LocalDateTime)ldt;
        }
    }
}
