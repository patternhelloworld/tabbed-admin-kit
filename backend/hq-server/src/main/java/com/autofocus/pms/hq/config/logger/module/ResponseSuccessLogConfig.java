package com.autofocus.pms.hq.config.logger.module;

import com.autofocus.pms.common.config.logger.common.CommonLoggingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/*
 *   resources/logback-spring.xml 전달 받아서 확인. (양식은 resources/logback-spring.xml.sample 참조)
 * */
@Aspect
@Component
public class ResponseSuccessLogConfig {

    private static final Logger logger = LoggerFactory.getLogger(ResponseSuccessLogConfig.class);


    // 표현식 설명 : com.autofocus.pms.hq.controller 패키지 및 하위 패키지(..)의 모든 메서드(*)
    @AfterReturning(pointcut = ("within(com.autofocus.pms.hq.domain..api..*)"),
            returning = "returnValue")
    public void endpointAfterReturning(JoinPoint p, Object returnValue) {

        boolean isErrored = false;
        String loggedText = "\n[After - Returning Thread] : " + Thread.currentThread().getId() + "\n";

        // 3. Response logging
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

            if (returnValue.getClass().equals(ResponseEntity.class)) {
                MediaType mediaType = ((ResponseEntity) returnValue).getHeaders().getContentType();

                if (mediaType != null && (mediaType.getType().equals("image") || mediaType.equals(MediaType.APPLICATION_OCTET_STREAM))) {
                    loggedText += "[After - Response] \n" + "Image binary";
                } else {
                    loggedText += "[After - Response] \n" + mapper.writeValueAsString(returnValue);
                }
            } else {
                loggedText += "[After - Response] \n" + mapper.writeValueAsString(returnValue);
            }

        } catch (Exception ex3) {
            isErrored = true;
            loggedText += "[After - Error during the responseLogging] : " + ex3.getMessage();
        }

        // 5. 발생한 객체
        try {
            loggedText += "\n[After - Location] : " + p.getTarget().getClass().getSimpleName() + " " + p.getSignature().getName();
        } catch (Exception ex5) {
            isErrored = true;
            loggedText += "\n[After - Error during the finalStage] : " + ex5.getMessage();
        }

        CommonLoggingRequest commonLoggingRequest = new CommonLoggingRequest();

        if (isErrored) {
            logger.error(commonLoggingRequest.getText() + loggedText + "|||\n");
        } else {
            logger.trace(commonLoggingRequest.getText() + loggedText + "|||\n");
        }


    }



}