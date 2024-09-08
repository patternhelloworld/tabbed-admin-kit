package com.autofocus.pms.common.enums;

public enum PrivacyNumberType {
    주민등록번호(1),
    사업자번호(2),
    카드번호(3);

    private final int value;

    PrivacyNumberType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PrivacyNumberType fromValue(int value) {
        for (PrivacyNumberType type : PrivacyNumberType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
