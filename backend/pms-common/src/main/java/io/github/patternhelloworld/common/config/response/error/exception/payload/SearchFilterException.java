package io.github.patternhelloworld.common.config.response.error.exception.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SearchFilterException extends JsonProcessingException{
	public SearchFilterException(String message){

		super(message);
	}
	public SearchFilterException(String message, Throwable cause) {
		super(message, cause);
	}
}
