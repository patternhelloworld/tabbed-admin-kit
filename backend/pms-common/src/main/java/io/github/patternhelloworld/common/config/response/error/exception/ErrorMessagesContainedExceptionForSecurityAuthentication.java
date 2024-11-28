package io.github.patternhelloworld.common.config.response.error.exception;


import io.github.patternhelloworld.common.config.response.error.dto.CommonErrorMessages;
import org.springframework.security.core.AuthenticationException;

public abstract class ErrorMessagesContainedExceptionForSecurityAuthentication extends AuthenticationException {

	protected CommonErrorMessages commonErrorMessages;

	public ErrorMessagesContainedExceptionForSecurityAuthentication(){
		super(null);
	}
	public ErrorMessagesContainedExceptionForSecurityAuthentication(String message){
		super(message);
	}
	public ErrorMessagesContainedExceptionForSecurityAuthentication(String message, Throwable cause) {
		super(message, cause);
	}
	public ErrorMessagesContainedExceptionForSecurityAuthentication(CommonErrorMessages commonErrorMessages){
		super(null);
		this.commonErrorMessages = commonErrorMessages;
	}
	public CommonErrorMessages getErrorMessages() {
		return commonErrorMessages;
	}
}
