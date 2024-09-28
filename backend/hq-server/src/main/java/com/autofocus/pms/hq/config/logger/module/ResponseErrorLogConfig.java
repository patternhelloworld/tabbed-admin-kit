package com.autofocus.pms.hq.config.logger.module;

import com.autofocus.pms.common.config.logger.common.CommonLoggingRequest;
import com.autofocus.pms.common.config.response.error.PmsCommonGlobalExceptionHandler;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;

import com.autofocus.pms.hq.config.response.error.CustomSecurityGlobalExceptionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/*
 *   resources/logback-spring.xml 전달 받아서 확인. (양식은 resources/logback-spring.xml.sample 참조)
 * */
@Aspect
@Component
public class ResponseErrorLogConfig {

    private static final Logger logger = LoggerFactory.getLogger(ResponseErrorLogConfig.class);


    @AfterReturning(pointcut = ("within(com.autofocus.pms.common.config.response.error..*) || within(com.autofocus.pms.security.oauth2.config.response.common.error..*) || within(com.autofocus.pms.hq.config.response.error..*)"),
            returning = "returnValue")
    public void endpointAfterExceptionReturning(JoinPoint p, Object returnValue) {

        String loggedText = "\n[After Throwing Thread] : " + Thread.currentThread().getId() + "\n";

        // 4. Error logging
        try {
            if (p.getTarget().getClass().equals(PmsCommonGlobalExceptionHandler.class) || p.getTarget().getClass().equals(CustomSecurityGlobalExceptionHandler.class)) {

                ErrorResponsePayload errorResponsePayload = (ErrorResponsePayload) ((ResponseEntity) returnValue).getBody();
                loggedText += String.format("[After - Error Response]\n message : %s || \n userMessage : %s || \n cause : %s || \n stackTrace : %s",
                        errorResponsePayload != null ? errorResponsePayload.getMessage() : "No error detail message",
                        errorResponsePayload != null ? errorResponsePayload.getUserMessage() : "No error detail auth message",
                        errorResponsePayload != null ? errorResponsePayload.getCause() : "No error detail cause",
                        errorResponsePayload != null ? errorResponsePayload.getStackTrace() : "No error detail stack trace");
            }
        } catch (Exception ex4) {

            loggedText += "\n[Error during the errorLogging] : " + ex4.getMessage();
        }

        // 5. 발생한 객체
        try {
            loggedText += "\n[Location] : " + p.getTarget().getClass().getSimpleName() + " " + p.getSignature().getName();
        } catch (Exception ex5) {
            loggedText += "\n[Error during the finalStage] : " + ex5.getMessage();
        }

        CommonLoggingRequest commonLoggingRequest = new CommonLoggingRequest();

        logger.error(commonLoggingRequest.getText() + loggedText + "|||\n");
    }


}