package io.github.patternhelloworld.common.config.response.error.exception.data;


import io.github.patternhelloworld.common.config.response.error.dto.CommonErrorMessages;
import io.github.patternhelloworld.common.config.response.error.exception.ErrorMessagesContainedException;

public class CustomIllegalStateException extends ErrorMessagesContainedException {
    public CustomIllegalStateException() {
    }

    public CustomIllegalStateException(String message) {
        super(message);
    }

    public CustomIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomIllegalStateException(CommonErrorMessages commonErrorMessages) {
        super(commonErrorMessages);
    }
}
