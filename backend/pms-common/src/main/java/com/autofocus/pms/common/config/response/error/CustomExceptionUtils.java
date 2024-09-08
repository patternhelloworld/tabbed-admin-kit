package com.autofocus.pms.common.config.response.error;

import com.autofocus.pms.common.config.logger.common.CommonLoggingRequest;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;
import com.autofocus.pms.common.config.response.error.message.GeneralExceptionMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomExceptionUtils {



    public static void createNonStoppableErrorMessage(String message, Logger logger) {

        logger.error("[NON-STOPPABLE ERROR] : ");

        try {
            CommonLoggingRequest commonLoggingRequest = new CommonLoggingRequest();
            logger.error(commonLoggingRequest.getText());
        } catch (Exception ex2) {
            logger.error(ex2.getMessage());
        } finally {
            ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(message, "Without error param " + " / Thread ID = " + Thread.currentThread().getId() + " / StackTrace",
                    message, "", "");

            logger.error(" / " + errorResponsePayload.toString());
        }

    }

    public static void createNonStoppableErrorMessage(String message, Throwable ex, Logger logger) {

        logger.error("[NON-STOPPABLE ERROR] : ");

        try {
            CommonLoggingRequest commonLoggingRequest = new CommonLoggingRequest();
            logger.error(commonLoggingRequest.getText());
        } catch (Exception ex2) {
            logger.error(ex2.getMessage());
        } finally {
            ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(message, "Without error param " + " / Thread ID = " + Thread.currentThread().getId() + " / StackTrace",
                    message, CustomExceptionUtils.getFirstTwoStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));

            logger.error(" / " + errorResponsePayload.toString());
        }

    }

    public static void createNonStoppableErrorMessage(String message, String ex, Logger logger){
        try {
            logger.error("[NON-STOPPABLE ERROR] : " + message + " / " + ex + " / " + " / Thread ID = " + Thread.currentThread().getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getAllCausesWithStartMessage(Throwable e, String causes) {
        if (e.getCause() == null) return causes;
        causes += e.getCause() + " / ";
        return getAllCausesWithStartMessage(e.getCause(), causes);
    }

    public static String getAllCauses(Throwable e) {
        String causes = "";
        return getAllCausesWithStartMessage(e, causes);
    }

    public static String getAllStackTraces(Throwable e) {
        return ExceptionUtils.getStackTrace(e);
    }

    public static String getFirstTwoStackTraces(Throwable e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        StringBuilder sb = new StringBuilder();

        // 스택 트레이스 배열의 길이를 확인하고 상위 두 개만 출력
        int count = Math.min(2, stackTraces.length);
        for (int i = 0; i < count; i++) {
            sb.append(stackTraces[i].toString());
            sb.append("\n");  // 각 스택 트레이스 요소를 새로운 줄에 출력
        }

        return sb.toString();
    }


    /*
    *   message 예시
    *   // could not execute statement; SQL [n/a]; constraint [point.chassis_number]
        // could not execute statement; SQL [n/a]; constraint [null]
    * */
    public static Map<String, String> convertDataIntegrityExceptionMessageToObjMySql(String message){

        String key = parseKeyFromUniqueDataIntegrityExceptionMessageMySql(message);
        if(key == null || key.equals("null")){
            return null;
        }

        String fieldUserMessage = GeneralExceptionMessage.DUPLICATE_VALUE_FOUND.getMessage();
        // 아래 건은 폐기 됨
        /* if(key.equals("customer_id_gift_id_requested_status")){
            fieldUserMessage = "해당 Gift 는 이미 요청된 상태입니다. 중복 구매를 방지하기 위해, 현재 요청된 기프트는 승인이 완료되면 재구매가 가능합니다.";
        }*/

        if(key.equals("qr_id")){
            fieldUserMessage = "이미 등록된 QR이 확인 되었습니다.";
        }else if(key.equals("customer.id_name") || key.equals("id_name")){
            fieldUserMessage = "중복된 ID 가 있습니다. 다른 ID를 입력해주세요.";
        }
        Map<String, String> map = new HashMap<>();
        map.put(key, fieldUserMessage);
        return map;
    }

    public static String parseKeyFromUniqueDataIntegrityExceptionMessageMySql(String message){
        Pattern pattern = Pattern.compile("constraint \\[([^\\u005D]+)\\]");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1).replaceAll("^[^\\.]+\\.", "");
        } else {
            return null;
        }
    }

    public static Map<String, String> convertDataIntegrityExceptionMessageToObjMSSql(String message){

        String key = parseKeyFromUniqueDataIntegrityExceptionMessageMSSql(message);
        if(key == null || key.equals("null")){
            return null;
        }

        String fieldUserMessage = GeneralExceptionMessage.DUPLICATE_VALUE_FOUND.getMessage();
        // 아래 건은 폐기 됨
        /* if(key.equals("customer_id_gift_id_requested_status")){
            fieldUserMessage = "해당 Gift 는 이미 요청된 상태입니다. 중복 구매를 방지하기 위해, 현재 요청된 기프트는 승인이 완료되면 재구매가 가능합니다.";
        }*/

        if(key.equals("qr_id")){
            fieldUserMessage = "이미 등록된 QR이 확인 되었습니다.";
        }else if(key.equals("customer.id_name") || key.equals("id_name")){
            fieldUserMessage = "중복된 ID 가 있습니다. 다른 ID를 입력해주세요.";
        }
        Map<String, String> map = new HashMap<>();
        map.put(key, fieldUserMessage);
        return map;
    }

    /*
    *   오류 메시지 예시들
    *   Violation of UNIQUE KEY constraint 'UQ__treatment__qr_id'. Cannot insert duplicate key in object 'dbo.treatment'. The duplicate key value is (343997).
    * */
    public static String parseKeyFromUniqueDataIntegrityExceptionMessageMSSql(String message){
        Pattern pattern = Pattern.compile("(?:unique index|UNIQUE KEY constraint) '[^']+__([^']+)'");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static Map<String, String> extractMethodArgumentNotValidErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }
}
