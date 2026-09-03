package org.dromara.surpass.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * 接口字段数据类型枚举
 */
@Getter
public enum ApiFieldDataTypeEnum {

    STRING("String", "字符串", v -> v),

    BYTE("Byte", "字节", v -> Byte.valueOf(v.toString())),

    SHORT("Short", "短整型", v -> Short.valueOf(v.toString())),

    INTEGER("Integer", "整数", v -> Integer.valueOf(v.toString())),

    LONG("Long", "长整数", v -> Long.valueOf(v.toString())),

    FLOAT("Float", "单精度浮点", v -> Float.valueOf(v.toString())),

    DOUBLE("Double", "双精度浮点", v -> Double.valueOf(v.toString())),

    BOOLEAN("Boolean", "布尔", v -> Boolean.valueOf(v.toString())),

    ARRAY_INTEGER("Array[Integer]", "整型数组", v -> {
        if (v == null) {
            return new ArrayList<Integer>();
        }
        
        // 如果已经是List<Integer>类型，直接返回
        if (v instanceof List<?>) {
            List<?> list = (List<?>) v;
            List<Integer> result = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Integer) {
                    result.add((Integer) item);
                } else if (item != null) {
                    result.add(Integer.valueOf(item.toString()));
                }
            }
            return result;
        }
        
        // 处理数组类型
        if (v.getClass().isArray()) {
            List<Integer> result = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(v);
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(v, i);
                if (item instanceof Integer) {
                    result.add((Integer) item);
                } else if (item != null) {
                    result.add(Integer.valueOf(item.toString()));
                }
            }
            return result;
        }
        
        // 处理JSON数组字符串
        String strValue = v.toString().trim();
        if (strValue.startsWith("[") && strValue.endsWith("]")) {
            // 移除方括号并分割
            String content = strValue.substring(1, strValue.length() - 1);
            if (content.isEmpty()) {
                return new ArrayList<Integer>();
            }
            String[] arr = content.split(",");
            List<Integer> result = new ArrayList<>();
            for (String s : arr) {
                String cleanStr = s.trim().replaceAll("^\"|\"$", ""); // 去除引号
                if (!cleanStr.isEmpty()) {
                    result.add(Integer.valueOf(cleanStr));
                }
            }
            return result;
        }
        
        // 处理逗号分隔的字符串
        String[] arr = StringUtils.commaDelimitedListToStringArray(strValue);
        List<Integer> result = new ArrayList<>();
        for (String s : arr) {
            String cleanStr = s.trim();
            if (!cleanStr.isEmpty()) {
                result.add(Integer.valueOf(cleanStr));
            }
        }
        return result;
    }),

    ARRAY_FLOAT("Array[Float]", "浮点数组", v -> {
        if (v == null) {
            return new ArrayList<Float>();
        }
        
        // 如果已经是List<Float>类型，直接返回
        if (v instanceof List<?>) {
            List<?> list = (List<?>) v;
            List<Float> result = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Float) {
                    result.add((Float) item);
                } else if (item != null) {
                    result.add(Float.valueOf(item.toString()));
                }
            }
            return result;
        }
        
        // 处理数组类型
        if (v.getClass().isArray()) {
            List<Float> result = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(v);
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(v, i);
                if (item instanceof Float) {
                    result.add((Float) item);
                } else if (item != null) {
                    result.add(Float.valueOf(item.toString()));
                }
            }
            return result;
        }
        
        // 处理JSON数组字符串
        String strValue = v.toString().trim();
        if (strValue.startsWith("[") && strValue.endsWith("]")) {
            // 移除方括号并分割
            String content = strValue.substring(1, strValue.length() - 1);
            if (content.isEmpty()) {
                return new ArrayList<Float>();
            }
            String[] arr = content.split(",");
            List<Float> result = new ArrayList<>();
            for (String s : arr) {
                String cleanStr = s.trim().replaceAll("^\"|\"$", ""); // 去除引号
                if (!cleanStr.isEmpty()) {
                    result.add(Float.valueOf(cleanStr));
                }
            }
            return result;
        }
        
        // 处理逗号分隔的字符串
        String[] arr = StringUtils.commaDelimitedListToStringArray(strValue);
        List<Float> result = new ArrayList<>();
        for (String s : arr) {
            String cleanStr = s.trim();
            if (!cleanStr.isEmpty()) {
                result.add(Float.valueOf(cleanStr));
            }
        }
        return result;
    }),

    ARRAY_STRING("Array[String]", "字符串数组", v -> {
        if (v == null) {
            return new ArrayList<String>();
        }
        
        // 如果已经是List<String>类型，直接返回
        if (v instanceof List<?>) {
            List<?> list = (List<?>) v;
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                result.add(item != null ? item.toString() : "");
            }
            return result;
        }
        
        // 处理数组类型
        if (v.getClass().isArray()) {
            List<String> result = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(v);
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(v, i);
                result.add(item != null ? item.toString() : "");
            }
            return result;
        }
        
        // 处理JSON数组字符串
        String strValue = v.toString().trim();
        if (strValue.startsWith("[") && strValue.endsWith("]")) {
            // 移除方括号并分割
            String content = strValue.substring(1, strValue.length() - 1);
            if (content.isEmpty()) {
                return new ArrayList<String>();
            }
            String[] arr = content.split(",");
            List<String> result = new ArrayList<>();
            for (String s : arr) {
                String cleanStr = s.trim().replaceAll("^\"|\"$", ""); // 去除引号
                result.add(cleanStr);
            }
            return result;
        }
        
        // 处理逗号分隔的字符串
        String[] arr = StringUtils.commaDelimitedListToStringArray(strValue);
        List<String> result = new ArrayList<>();
        for (String s : arr) {
            result.add(s.trim());
        }
        return result;
    });

    private final String code;
    private final String description;

    /**
     * 转换函数
     */
    private final Function<Object, Object> converter;

    ApiFieldDataTypeEnum(String code,
                         String description,
                         Function<Object, Object> converter) {
        this.code = code;
        this.description = description;
        this.converter = converter;
    }

    /**
     * 类型转换
     */
    public Object convert(Object value) {
        return converter.apply(value);
    }

    /**
     * 根据code获取枚举
     */
    public static ApiFieldDataTypeEnum fromCode(String code) {
        for (ApiFieldDataTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
