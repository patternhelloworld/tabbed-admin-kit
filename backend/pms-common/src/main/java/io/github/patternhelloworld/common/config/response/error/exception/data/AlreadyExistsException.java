package io.github.patternhelloworld.common.config.response.error.exception.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
