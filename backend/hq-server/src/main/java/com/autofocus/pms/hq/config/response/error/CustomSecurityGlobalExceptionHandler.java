package com.autofocus.pms.hq.config.response.error;


import com.autofocus.pms.common.config.response.error.CustomExceptionUtils;
import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;
import com.autofocus.pms.common.util.OrderConstants;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/*
*   GlobalExceptionHandler 진입 현재 우선 순위 : 이 모듈 > pms-security-oauth2 > pms-common
*    : 아래 비번 틀릴 경우의 추가적 처리를 위해, 현재 여기를 최우선으로 함
* */
@Order(OrderConstants.PMS_COMMON_EXCEPTION_HANDLER_ORDER - 2)
@ControllerAdvice
@RequiredArgsConstructor
public class CustomSecurityGlobalExceptionHandler {

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;
    private final UserRepository userRepository;

    // 401 : Authentication
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> authenticationException(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload;
        if(ex instanceof CustomOauth2AuthenticationException && ((CustomOauth2AuthenticationException) ex).getErrorMessages() != null) {

            ErrorMessages errorMessages = ((CustomOauth2AuthenticationException) ex).getErrorMessages();
            if(errorMessages.getUserMessage().equals(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE.getMessage()) ||
                    errorMessages.getUserMessage().equals(DefaultSecurityUserExceptionMessage.AUTHENTICATION_PASSWORD_FAILED_LIMIT_EXCEEDED.getMessage())){

                String username = errorMessages.getUserDetails().getUsername();

                // 비번 틀릴 경우 failedCount 추가
                User user = userRepository.findByUserId(username).orElse(null);
                if(user != null){
                    user.getPassword().setFailedCount(user.getPassword().getFailedCount() + 1);
                    // DB에 저장됨
                    userRepository.save(user);
                    // DB에 저장된 값 : user.getPassword().getFailedCount()
                   ((CustomOauth2AuthenticationException) ex).getErrorMessages().setUserMessage(((CustomOauth2AuthenticationException) ex).getErrorMessages().getUserMessage() + " (실패 횟수 : " + user.getPassword().getFailedCount() + ". 관리자에게 초기화를 요청 하십시오.)");
                    errorResponsePayload = new ErrorResponsePayload(((CustomOauth2AuthenticationException) ex).getErrorMessages(),
                            ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                            CustomExceptionUtils.getAllCauses(ex), null);
                }else{
                    errorResponsePayload = new ErrorResponsePayload(CustomExceptionUtils.getAllCauses(ex), request.getDescription(false), iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE),
                            ex.getMessage() +  " !!! 사용자 (" + username + ") 미발견. 로직상 문제 없는 지, 시스템 관리자 로그 확인 필요.", ex.getStackTrace()[0].toString());
                }
            }else{
                errorResponsePayload = new ErrorResponsePayload(((CustomOauth2AuthenticationException) ex).getErrorMessages(),
                        ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                        CustomExceptionUtils.getAllCauses(ex), null);
            }


        }else {
            errorResponsePayload = new ErrorResponsePayload(CustomExceptionUtils.getAllCauses(ex), request.getDescription(false), iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE),
                    ex.getMessage(), ex.getStackTrace()[0].toString());
        }
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNAUTHORIZED);
    }

    // 403 : Authorization
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> authorizationException(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage() != null ? ex.getMessage() : CustomExceptionUtils.getAllCauses(ex), request.getDescription(false),
                ex.getMessage() == null || ex.getMessage().equals("Access Denied") ? iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHORIZATION_FAILURE) : ex.getMessage(), ex.getStackTrace()[0].toString());
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.FORBIDDEN);
    }

    /*
    *   Social Login (Access Token Failure)
    * */
    // 1. NoSocialRegisteredException: Trying to do social login but the user does not exist (TO DO. Need separation. The app is branching based on the message of this Exception)
    // 2. AlreadySocialRegisteredException: Trying to create a social user but it already exists
    @ExceptionHandler({ AlreadySocialRegisteredException.class, NoSocialRegisteredException.class })
    public ResponseEntity<?> socialLoginFailureException(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false), ex.getMessage(),
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNAUTHORIZED);
    }
    // SocialEmailNotProvidedException: The app received a 200 status from the social platform using the access token store, but the social platform did not provide the user's email information. In this case, the company needs to obtain authorization from the social platform.
    @ExceptionHandler({ SocialEmailNotProvidedException.class})
    public ResponseEntity<?> accessToSocialSuccessButIssuesWithReturnedValue(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false), ex.getMessage(),
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.FORBIDDEN);
    }

    // Social Resource Access Failure (Access Token OK but No Permission)
    // The social platform has blocked access to the requested resource.
    @ExceptionHandler({SocialUnauthorizedException.class})
    public ResponseEntity<?> accessToSocialDenied(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage() != null ? ex.getMessage() : CustomExceptionUtils.getAllCauses(ex),
                request.getDescription(false),"Not a valid access token." , ex.getStackTrace()[0].toString());
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.FORBIDDEN);
    }

    // OTP (Only for Admin)
    @ExceptionHandler({OtpValueUnauthorizedException.class})
    public ResponseEntity<?> otpException(Exception ex, WebRequest request) {

        Map<String, String> userValidationMessages = new HashMap<>();
        userValidationMessages.put("otp_value", ex.getMessage());

        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage(), request.getDescription(false),
                null,
                userValidationMessages,
                CustomExceptionUtils.getAllStackTraces(ex), CustomExceptionUtils.getAllCauses(ex));

        return new ResponseEntity<>(errorResponsePayload, HttpStatus.UNAUTHORIZED);
    }

    // UserDeletedException : caused by the process of user deactivation
    // UserRestoredException : caused by the process of user reactivation
    @ExceptionHandler({UserDeletedException.class, UserRestoredException.class})
    public ResponseEntity<?> activationException(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ex.getMessage() != null ? ex.getMessage() : CustomExceptionUtils.getAllCauses(ex),
                request.getDescription(false),ex.getMessage() , ex.getStackTrace()[0].toString());
        return new ResponseEntity<>(errorResponsePayload, HttpStatus.FORBIDDEN);
    }

}
