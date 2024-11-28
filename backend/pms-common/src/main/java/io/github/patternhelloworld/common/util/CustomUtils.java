package io.github.patternhelloworld.common.util;


import io.github.patternhelloworld.common.encryption.KISA_SEED_ECB;
import io.github.patternhelloworld.common.encryption.EncKeyConstant;
import io.github.patternhelloworld.common.enums.PrivacyNumberType;
import io.github.patternhelloworld.common.enums.MobileOSType;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomUtils {

    public static boolean isEmpty(Object obj) {
        if (obj == null) { return true; }
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }
        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); }
        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }
        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); }

        return false;
    }

    public static String createUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createSequentialUUIDStringReplaceHyphen(){
        return CustomUtils.createSequentialUUIDString().replace("-", "");
    }

    public static String createSequentialUUIDString(){
        byte[] randomBytes = new byte[10];
        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(randomBytes);

        long timestamp = Instant.now().getEpochSecond() / 10000L;

        byte[] timestampBytes = BitConverter.getBytes(timestamp);

        if(BitConverter.IsLittleEndian())
            Collections.reverse(Arrays.asList(timestampBytes));

        byte[] guidBytes = new byte[16];

        System.arraycopy(timestampBytes, 2, guidBytes, 0, 6);
        System.arraycopy(randomBytes, 0, guidBytes, 6, 10);

        if (BitConverter.IsLittleEndian()) {
            // 0-3 reverse
            reverse(guidBytes, 0, 4);
            // 4-5 reverse
            reverse(guidBytes, 4, 2);
        }

        return UUID.nameUUIDFromBytes(guidBytes).toString();
    }

    public static void reverse(byte[] contents, int index, int length){
        byte temp;
        for(int idx = index; idx < index + length; idx++){
            temp = contents[idx];
            contents[idx] = contents[contents.length - idx - 1];
            contents[contents.length - idx - 1] = temp;
        }
    }


    public static <T> Optional<T> getAsOptional(List<T> list, int index) {
        try {
            return Optional.of(list.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }



    public static LocalDateTime convertDateStrToLocalDateTime(String t, int h, int m, int s){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(t, formatter);
        LocalDateTime dateTime = date.atTime(h, m, s);

        return dateTime;
    }

    public static LocalDateTime convertDateStrToLocalDateTime(String t){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(t, formatter);
        return dateTime;
    }

    public static LocalDate convertDateStrToLocalDate(String t){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(t, formatter);

        return date;
    }

    public static String removeSpecialCharacters(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    public static String maskIdName(String idName) {
        if (idName == null || idName.length() == 0) {
            return "";
        }

        int unmaskedLength = (int) (idName.length() * 0.3);
        String unmaskedPart = idName.substring(0, unmaskedLength);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < idName.length() - unmaskedLength; i++) {
            result.append("*");
        }
        String maskedPart = result.toString();
        return unmaskedPart + maskedPart;
    }

    public static Integer[] commaSplitStrToIntegerArr(String input){

        // 콤마(,)를 기준으로 문자열 분리
        String[] parts = input.split(",");

        // 변환된 Integer 배열을 담기 위한 배열 생성
        Integer[] integers = new Integer[parts.length];

        // 문자열을 Integer로 변환하여 배열에 저장
        for (int i = 0; i < parts.length; i++) {
            integers[i] = Integer.parseInt(parts[i].trim()); // trim()을 사용해 공백 제거
        }

        return integers;
    }

    public static MobileOSType getMobileOperatingSystem(String userAgent) {
        if (userAgent != null && userAgent.matches(".*Windows Phone.*")) {
            return MobileOSType.WINDOWS_PHONE;
        }
        if (userAgent != null && userAgent.matches(".*Android.*")) {
            return MobileOSType.ANDROID;
        }
        if (userAgent != null && (userAgent.matches(".*iPad.*") || userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPod.*") || userAgent.matches(".*CFNetwork.*") || userAgent.matches(".*Darwin.*"))) {
            return MobileOSType.IOS;
        }
        return MobileOSType.UNKNOWN;
    }

    public static String encryptECB(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String replacedStr = str.replaceAll("-", "");
        char[] planChars = new char[replacedStr.length()];
        for(int i = 0 ; i < replacedStr.length() ; i++) {
            planChars[i] = replacedStr.charAt(i);
        }

        byte[] planBytes = new byte[planChars.length];
        if(planChars instanceof char[]) {
            for(int i = 0 ; i < planChars.length ; i++) {
                planBytes[i] = (byte) Integer.parseInt(String.valueOf(planChars[i]), 16);
            }
        } else {
            return str;
        }

        String[] keyStr = EncKeyConstant.BSZ_USER_KEY.split(",");
        byte[] keyBytes = new byte[keyStr.length];
        for (int i = 0; i < keyStr.length; i++) {
            keyBytes[i] = (byte) Integer.parseInt(keyStr[i],16);
        }

        byte[] resultByte;
        try {
            resultByte = KISA_SEED_ECB.SEED_ECB_Encrypt(keyBytes, planBytes, 0, planBytes.length);
        } catch (RuntimeException e) {
            //throw new RuntimeException("Encryption failed", e);
            return str;
        }

        StringBuilder resultStr = new StringBuilder();
        for(int i = 0 ; i < resultByte.length ; i++) {
            if(i == resultByte.length - 1) {
                resultStr.append(String.format("%02X", resultByte[i]));
            } else {
                resultStr.append(String.format("%02X", resultByte[i])).append(",");
            }
        }

        return resultStr.toString();
    }

    public static String decryptECB(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String[] planStr = str.split(",");
        byte[] planBytes = new byte[planStr.length];

        for (int i = 0; i < planStr.length; i++) {
            try {
                planBytes[i] = (byte) Integer.parseInt(planStr[i],16);
            } catch (NumberFormatException ne) {
                return null;
            }
        }

        if (planBytes.length == 0) {
            return null;
        }

        String[] keyStr =  EncKeyConstant.BSZ_USER_KEY.split(",");
        byte[] keyBytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            keyBytes[i] = (byte) Integer.parseInt(keyStr[i], 16);
        }

        byte[] resultByte;
        try {
            resultByte = KISA_SEED_ECB.SEED_ECB_Decrypt(keyBytes, planBytes, 0, planBytes.length);
        } catch (RuntimeException e) {
            //throw new RuntimeException("Decryption failed", e);
            return null;
        }

        StringBuilder resultStr = new StringBuilder();
        try {
            for(byte b : resultByte) {
                resultStr.append(b);
            }
        } catch (RuntimeException e) {
            return null;
        }

        return resultStr.toString();
    }

    public static String makeNumberPattern(String number, int type) {

        if (number == null || number.isEmpty()) {
            return null;
        }

        String result = "";
        PrivacyNumberType numberType = PrivacyNumberType.fromValue(type);

        switch(numberType) {
            case 주민등록번호 -> {
                if (number.length() != 13) {
                    break;
                }
                result += number.substring(0,6) + "-" + number.substring(6);
            }
            case 사업자번호 -> {
                if (number.length() != 10) {
                    break;
                }
                result += number.substring(0,3) + "-" + number.substring(3,5) + "-" + number.substring(5);
            }
            case 카드번호 -> {
                if (number.length() != 16) {
                    break;
                }
                result += number.substring(0,4) + "-" + number.substring(4,8) + "-" + number.substring(8,12) + "-" + number.substring(12);
            }
        }

        if(result.equals("")) {
            result = number;
        }

        return result;
    }
}
