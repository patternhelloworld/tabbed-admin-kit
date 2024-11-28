package io.github.patternhelloworld.tak.config.response.error;


import io.github.patternhelloworld.common.config.response.error.CustomExceptionUtils;
import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepository;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;

import io.github.patternknife.securityhelper.oauth2.api.config.security.message.ISecurityUserExceptionMessageService;


import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.dto.KnifeErrorMessages;
import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.dto.SecurityKnifeErrorResponsePayload;
import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException;
import io.github.patternknife.securityhelper.oauth2.api.config.util.OrderConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/*
*   GlobalExceptionHandler 진입 현재 우선 순위 : 이 모듈 > pms-security-oauth2 > pms-common
*    : 아래 비번 틀릴 경우의 추가적 처리를 위해, 현재 여기를 최우선으로 함
* */
@Order(OrderConstants.SECURITY_KNIFE_EXCEPTION_HANDLER_ORDER - 2)
@ControllerAdvice
@RequiredArgsConstructor
public class CustomSecurityGlobalExceptionHandler {

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;
    private final UserRepository userRepository;

    // 401 : Authentication
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> authenticationException(Exception ex, WebRequest request) {
        SecurityKnifeErrorResponsePayload errorResponsePayload;
        if(ex instanceof KnifeOauth2AuthenticationException && ((KnifeOauth2AuthenticationException) ex).getErrorMessages() != null) {

            KnifeErrorMessages errorMessages = ((KnifeOauth2AuthenticationException) ex).getErrorMessages();
            if(errorMessages.getUserMessage().equals(CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE.getMessage()) ||
                    errorMessages.getUserMessage().equals(CustomSecurityUserExceptionMessage.AUTHENTICATION_PASSWORD_FAILED_EXCEEDED.getMessage())){

                String username = errorMessages.getUserDetails().getUsername();

                // 비번 틀릴 경우 failedCount 추가
                User user = userRepository.findByUserId(username).orElse(null);
                if(user != null){
                    user.getPassword().setFailedCount(user.getPassword().getFailedCount() + 1);
                    // DB에 저장됨
                    userRepository.save(user);
                    // DB에 저장된 값 : user.getPassword().getFailedCount()
                   ((KnifeOauth2AuthenticationException) ex).getErrorMessages().setUserMessage(((KnifeOauth2AuthenticationException) ex).getErrorMessages().getUserMessage() + " (실패 횟수 : " + user.getPassword().getFailedCount() + ". 관리자에게 초기화를 요청 하십시오.)");
                    errorResponsePayload = new SecurityKnifeErrorResponsePayload(((KnifeOauth2AuthenticationException) ex).getErrorMessages(),
                            ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                            CustomExceptionUtils.getAllCauses(ex), null);
                }else{
                    errorResponsePayload = new SecurityKnifeErrorResponsePayload(CustomExceptionUtils.getAllCauses(ex), request.getDescription(false), iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE),
                            ex.getMessage() +  " !!! 사용자 (" + username + ") 미발견. 로직상 문제 없는 지, 시스템 관리자 로그 확인 필요.", ex.getStackTrace()[0].toString());
                }
            }else{
                errorResponsePayload = new SecurityKnifeErrorResponsePayload(((KnifeOauth2AuthenticationException) ex).getErrorMessages(),
                        ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                        CustomExceptionUtils.getAllCauses(ex), null);
            }

            errorResponsePayload = new SecurityKnifeErrorResponsePayload(((KnifeOauth2AuthenticationException) ex).getErrorMessages(),
                    ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                    CustomExceptionUtils.getAllCauses(ex), null);


        }else {
            errorResponsePayload = new SecurityKnifeErrorResponsePayload(CustomExceptionUtils.getAllCauses(ex), request.getDescription(false), iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE),
                    ex.getMessage(), ex.getStackTrace()[0].toString());
        }
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNAUTHORIZED);
    }

}
