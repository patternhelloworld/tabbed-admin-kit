package io.github.patternhelloworld.common.config.response.error.exception.payload;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DaouHandledException extends JsonProcessingException{
	public DaouHandledException(String message){

		super(message);
	}
	public DaouHandledException(String message, Throwable cause) {
		super(message, cause);
	}
}
