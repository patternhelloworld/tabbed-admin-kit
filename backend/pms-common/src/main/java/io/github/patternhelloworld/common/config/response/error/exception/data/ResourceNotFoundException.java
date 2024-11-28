package io.github.patternhelloworld.common.config.response.error.exception.data;


import io.github.patternhelloworld.common.config.response.error.dto.CommonErrorMessages;
import io.github.patternhelloworld.common.config.response.error.exception.ErrorMessagesContainedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ErrorMessagesContainedException {
	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(CommonErrorMessages commonErrorMessages) {
		super(commonErrorMessages);
	}
}
