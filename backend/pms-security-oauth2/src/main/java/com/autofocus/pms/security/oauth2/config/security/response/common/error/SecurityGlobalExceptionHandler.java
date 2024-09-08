package com.autofocus.pms.security.oauth2.config.security.response.common.error;


import com.autofocus.pms.common.config.response.error.CustomExceptionUtils;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;
import com.autofocus.pms.common.util.OrderConstants;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.*;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
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

@Order(OrderConstants.PMS_COMMON_EXCEPTION_HANDLER_ORDER - 1)
@ControllerAdvice
@RequiredArgsConstructor
public class SecurityGlobalExceptionHandler {

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    // 401 : Authentication
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> authenticationException(Exception ex, WebRequest request) {
        ErrorResponsePayload errorResponsePayload;
        if(ex instanceof CustomOauth2AuthenticationException && ((CustomOauth2AuthenticationException) ex).getErrorMessages() != null) {
            errorResponsePayload = new ErrorResponsePayload(((CustomOauth2AuthenticationException) ex).getErrorMessages(),
                    ex, request.getDescription(false), CustomExceptionUtils.getAllStackTraces(ex),
                    CustomExceptionUtils.getAllCauses(ex), null);
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
