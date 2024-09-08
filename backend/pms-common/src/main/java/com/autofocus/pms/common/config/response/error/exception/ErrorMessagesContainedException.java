package com.autofocus.pms.common.config.response.error.exception;


import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;

public abstract class ErrorMessagesContainedException extends RuntimeException {

	protected ErrorMessages errorMessages;

	public ErrorMessagesContainedException(){

	}
	public ErrorMessagesContainedException(String message){
		super(message);
	}
	public ErrorMessagesContainedException(String message, Throwable cause) {
		super(message, cause);
	}
	public ErrorMessagesContainedException(ErrorMessages errorMessages){
		this.errorMessages = errorMessages;
	}
	public ErrorMessages getErrorMessages() {
		return errorMessages;
	}
}
