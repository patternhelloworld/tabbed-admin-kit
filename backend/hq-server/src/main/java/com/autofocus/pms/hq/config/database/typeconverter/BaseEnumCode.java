package com.autofocus.pms.hq.config.database.typeconverter;

public interface BaseEnumCode<T> {
    T getValue();

    static <E extends Enum<E> & BaseEnumCode<T>, T> E valueOf(Class<E> enumClass, T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value + " in " + enumClass.getName());
    }
}