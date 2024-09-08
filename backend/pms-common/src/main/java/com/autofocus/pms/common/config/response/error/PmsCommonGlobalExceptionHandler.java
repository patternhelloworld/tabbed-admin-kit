package com.autofocus.pms.common.config.response.error;


import com.autofocus.pms.common.config.database.SelectablePersistenceConst;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;
import com.autofocus.pms.common.config.response.error.exception.data.*;
import com.autofocus.pms.common.config.response.error.exception.file.FileNotFoundException;
import com.autofocus.pms.common.config.response.error.exception.payload.DaouHandledException;
import com.autofocus.pms.common.config.response.error.exception.payload.SearchFilterException;
import com.autofocus.pms.common.config.response.error.message.GeneralExceptionMessage;
import com.autofocus.pms.common.util.OrderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.HeuristicCompletionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/*
*
* */
@Order(OrderConstants.PMS_COMMON_EXCEPTION_HANDLER_ORDER)
@ControllerAdvice
public class PmsCommonGlobalExceptionHandler {

    // Data
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload;
        if(ex.getErrorMessages() != null){

            errorResponsePayload = new ErrorResponsePayload(ex.getErrorMessages(),
                    ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                    CustomExceptionUtils.getAllCauses(ex), null);

        }else{
            errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                    ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex),
                    CustomExceptionUtils.getAllCauses(ex));
        }
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(NoUpdateTargetException.class)
    public ResponseEntity<?> noUpdateTargetException(NoUpdateTargetException ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), ex.getStackTrace()[0].toString());
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SearchFilterException.class)
    public ResponseEntity<?> searchFilterException(SearchFilterException ex, WebRequest request) {

        //logger.error(ex.getMessage());
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getCause().getMessage(), request.getDescription(false),
                ex.getMessage(), ex.getStackTrace()[0].toString());
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerException(NullPointerException ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                GeneralExceptionMessage.NULL_VALUE_FOUND.getUserMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.NOT_FOUND);
    }
    

    @ExceptionHandler(PreconditionFailedException.class)
    public ResponseEntity<?> preconditionFailedException(PreconditionFailedException ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex),
                CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HeuristicCompletionException.class)
    public ResponseEntity<?> heuristicCompletionException(HeuristicCompletionException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                "JPA 처리되지 않은 오류입니다.", CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({FileNotFoundException.class, java.io.FileNotFoundException.class})
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex),
                CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.NOT_FOUND);

    }


    // Request @Valid 와 같은 Spring 자체의 유효성 검증

    /* 1. request body 를 검증( @RequestParam 이 아닌 경우) : @Valid 가 토스 : Throw 되는 오류 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> userValidationMessages = CustomExceptionUtils.extractMethodArgumentNotValidErrors(ex);

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                null,
                userValidationMessages,
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // request body 의 개별 파라매터들의 타입 (String, Date, Integer) 이 다르거나, json 양식 오류.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                "전달 받은 양식이 일치하지 않습니다. 다음 내용을 관리자에게 문의하십시오. (오류 내용 : " + ex.getMostSpecificCause().getMessage() + ")",
                null,
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.BAD_REQUEST);
    }

    /* 2-1. @RequestParam 인 경우 : @Validated 가 토스 : 유효성 검사  */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> missingServletRequestParameterException(ConstraintViolationException ex, WebRequest request, HttpServletRequest h) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getConstraintViolations().stream().findAny().isPresent() ? ex.getConstraintViolations().stream().findAny().get().getMessage() : ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    /* 2-2. @RequestParam 인 경우 : Contoller 의 @RequestParam 이 아예 없을 경우 Throw 되는 오류 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> missingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request, HttpServletRequest h) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                "필수 파라매터 (" + ex.getParameterName()  + ") 항목이 없습니다.", CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /* 3. 기타 Custom Validation : ex) @ValidPart 로 소스에서 검색  */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindExceptionHandler(BindException ex, WebRequest request) {

        Map<String, String> errorMessages = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false), null,
                errorMessages,
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    // 4. DB에서 조회(select) 에서 유효성을 검사

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> alreadyExistsException(AlreadyExistsException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateException(IllegalStateException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.BAD_REQUEST);
    }


    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dbDialect;
    /*
    *   5. DB 레이어에서 토스하는 유효성
    *     - 양/음수, Unique Key 등과 같은 동시성 문제를 방지하는 조건들로 유효성을 검사. (1차적으로는 select 문을 사용하는 것을 권장)
    * */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        //DataIntegrityViolationException - 데이터의 삽입/수정이 무결성 제약 조건을 위반할 때 발생하는 예외이다.
        //logger.error(ex.getMessage());

        Map<String, String> userValidationMessages = null;
        String userMessage = null;

        /*  1. POINT 컬럼은 부호 없음으로 해서 음수를 막음. */
        String signedErrorMsg = dbDialect.equals(SelectablePersistenceConst.MYSQL_8.getValue()) ?
                "Data truncation: Out of range value for column 'current_point' at row 1" :
                "CK__customer__current_point";
        /* 2. FK ON delete restrict 를 걸이서 사용중인 항목 삭제 금지 */
        String deleteConstraintKeyErrorMsg = dbDialect.equals(SelectablePersistenceConst.MYSQL_8.getValue()) ?
                "Cannot delete or update a parent row: a foreign key constraint fails" :
                "Arithmetic overflow error";

        if(ex.getMostSpecificCause().getMessage() != null
                    && ex.getMostSpecificCause().getMessage().contains(signedErrorMsg)){
            /* Data truncation: Out of range value for column 'current_point' at row 1 */
            /* Customer 테이블에서 point 컬럼이 음수가 될 경우 exception */
            /* PointDetailRepositorySupport 클래스의 주석 참조 */
            userValidationMessages = new HashMap<>();
            userValidationMessages.put("point", "포인트가 부족합니다.");

        }else if(ex.getMostSpecificCause().getMessage() != null
                && ex.getMostSpecificCause().getMessage().contains(deleteConstraintKeyErrorMsg)){
            userValidationMessages = new HashMap<>();
            userValidationMessages.put("id", "다른 곳에서 사용 중이라 삭제가 불가합니다.");

        }else{
            /*  UNIQUE, NULL */
            if(dbDialect.equals(SelectablePersistenceConst.MYSQL_8.getValue()) || dbDialect.equals("com.autofocus.pms.hq.config.database.dialect.CustomMySQL8Dialect")) {
                // 1. UNIQUE 부터 검사
                userValidationMessages = CustomExceptionUtils.convertDataIntegrityExceptionMessageToObjMySql(ex.getMessage());
                if (userValidationMessages == null) {
                    // 2. NULL 값에 해당하는 오류
                    userMessage = GeneralExceptionMessage.NULL_VALUE_FOUND.getMessage();
                    userValidationMessages = null;
                }
            }else{
                if(ex.getMostSpecificCause().getMessage() != null){
                    userValidationMessages = CustomExceptionUtils.convertDataIntegrityExceptionMessageToObjMSSql(ex.getMostSpecificCause().getMessage());
                    if (userValidationMessages == null) {
                        // 2. NULL 값에 해당하는 오류
                        userMessage = GeneralExceptionMessage.NULL_VALUE_FOUND.getMessage();
                        userValidationMessages = null;
                    }
                }
            }
        }

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                userMessage,
                userValidationMessages,
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));

        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AlreadyInProgressException.class)
    public ResponseEntity<?> alreadyInProgressException(AlreadyInProgressException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // 기프트-포인트 업체 (중요도 높아서 별도로...)
    @ExceptionHandler(DaouHandledException.class)
    public ResponseEntity<?> daouRequestException(DaouHandledException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                ex.getMessage(), CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // config/resttemplate 참조
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<?> restTemplateAccessException(ResourceAccessException ex, WebRequest request) {

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                "불편을 끼쳐 드려 송구합니다. 3rd Party API 제공 업체의 호출에 실패하였습니다. 문제가 지속되면 고객센터에 문의 주십시오.", CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.REQUEST_TIMEOUT);
    }


    // 마지막 : Unhandled
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unhandledExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload;
        String allStackTraces = CustomExceptionUtils.getAllStackTraces(ex);
        if (allStackTraces.contains("SocialOauthWeb.java")) {
            errorResponsePayload = new ErrorResponsePayload("", request.getDescription(false), GeneralExceptionMessage.UNHANDLED_ERROR.getUserMessage(),
                    "", "");
        }else{
            errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false), GeneralExceptionMessage.UNHANDLED_ERROR.getUserMessage(),
                    CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        }
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
