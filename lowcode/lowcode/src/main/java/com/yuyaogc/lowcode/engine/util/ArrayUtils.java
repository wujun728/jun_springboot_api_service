package com.yuyaogc.lowcode.engine.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 数组工具
 * 
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    /**
     * 转为 {@link ArrayList}
     * 
     * @param <T>
     * @param a
     * @return
     */
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        List<T> list = new ArrayList<>(a.length);
        for (T i : a) {
            list.add(i);
        }
        return list;
    }

    /**
     * 
     * @param collections
     * @return
     */
    public static List<List<Object>> zip(Iterable<?>... collections) {
        List<List<Object>> result = new ArrayList<>();
        List<Iterator<?>> all = new ArrayList<>(collections.length);
        for (Iterable<?> c : collections) {
            all.add(c.iterator());
        }
        while (true) {
            Object[] objs = new Object[collections.length];
            int index = 0;
            boolean hasValue = false;
            for (Iterator<?> iterator : all) {
                if (iterator.hasNext()) {
                    objs[index++] = iterator.next();
                    hasValue = true;
                } else {
                    hasValue = false;
                    break;
                }
            }
            if (!hasValue) {
                break;
            }
            result.add(ArrayUtils.asList(objs));
        }
        return result;
    }

    public static List<List<Object>> zip(Object[]... arrays) {
        List<List<Object>> result = new ArrayList<>();
        int row = 0;
        while (true) {
            Object[] objs = new Object[arrays.length];
            int index = 0;
            boolean hasValue = false;
            for (Object[] array : arrays) {
                if (array.length > row) {
                    objs[index++] = array[row];
                    hasValue = true;
                } else {
                    hasValue = false;
                    break;
                }
            }
            if (!hasValue) {
                break;
            }
            result.add(ArrayUtils.asList(objs));
            row++;
        }
        return result;
    }
}
