package io.github.patternhelloworld.common.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    // updateClinicField 에서 호출하여 Java Reflection 방법으로 Get Set
    public static String getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            return value != null ? value.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Handle or log the exception as needed
            return null;
        }
    }
    public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);

    }

}
