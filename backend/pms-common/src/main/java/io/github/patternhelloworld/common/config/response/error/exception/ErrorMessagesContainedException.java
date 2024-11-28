package io.github.patternhelloworld.common.config.response.error.exception;


import io.github.patternhelloworld.common.config.response.error.dto.CommonErrorMessages;

public abstract class ErrorMessagesContainedException extends RuntimeException {

	protected CommonErrorMessages commonErrorMessages;

	public ErrorMessagesContainedException(){

	}
	public ErrorMessagesContainedException(String message){
		super(message);
	}
	public ErrorMessagesContainedException(String message, Throwable cause) {
		super(message, cause);
	}
	public ErrorMessagesContainedException(CommonErrorMessages commonErrorMessages){
		this.commonErrorMessages = commonErrorMessages;
	}
	public CommonErrorMessages getErrorMessages() {
		return commonErrorMessages;
	}
}
