package io.github.patternhelloworld.tak.config.securityimpl.message;


import io.github.patternknife.securityhelper.oauth2.api.config.security.message.ExceptionMessageInterface;

public enum CustomSecurityUserExceptionMessage implements ExceptionMessageInterface {

    AUTHENTICATION_LOGIN_FAILURE("Authentication information is not valid. Please check and try again."),
    AUTHENTICATION_LOGIN_ERROR("An error occurred during authentication. If the problem persists, please contact customer service."),
    AUTHENTICATION_TOKEN_FAILURE("The authentication token has expired. Please log in again."),
    AUTHENTICATION_TOKEN_ERROR("There was a problem verifying the authentication token. Please log in again."),
    AUTHORIZATION_FAILURE("You do not have access permissions. Please request this from the administrator."),
    AUTHORIZATION_ERROR("An error occurred with access permissions. If the problem persists, please contact customer service."),

    // ID PASSWORD
    AUTHENTICATION_ID_NO_EXISTS("The specified ID does not exist."),
    AUTHENTICATION_WRONG_ID_PASSWORD("User information could not be verified. Please check your ID or password. If the problem persists, please contact customer service."),
    AUTHENTICATION_PASSWORD_FAILED_EXCEEDED("The number of password attempts has been exceeded."),

    // Wrong Authorization Code
    AUTHENTICATION_INVALID_RESPONSE_TYPE("The specified Response Type is invalid."),
    AUTHENTICATION_INVALID_AUTHORIZATION_CODE("The specified Authorization Code is invalid."),
    AUTHENTICATION_INVALID_REDIRECT_URI("The specified Redirect URI is invalid."),
    AUTHENTICATION_SCOPES_NOT_APPROVED("The specified Scopes are not approved."),
    // CLIENT ID, SECRET
    AUTHENTICATION_WRONG_CLIENT_ID_SECRET("Client information is not verified."),

    // GRANT TYPE
    AUTHENTICATION_WRONG_GRANT_TYPE("Wrong Grant Type detected.");

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomSecurityUserExceptionMessage(String message) {
        this.message = message;
    }

}
