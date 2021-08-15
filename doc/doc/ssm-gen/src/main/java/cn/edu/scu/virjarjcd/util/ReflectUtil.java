package cn.edu.scu.virjarjcd.util;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static void addField(Object obj, String key, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static <T> T getField(Object obj, String key) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class clazz = obj.getClass();
        Field field = null;
        do {
            try {
                field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (NoSuchFieldException e) {
                //do nothing
            }
        }
        while ((clazz = clazz.getSuperclass()) != null);

        throw  new NoSuchFieldException(key);
    }
}
