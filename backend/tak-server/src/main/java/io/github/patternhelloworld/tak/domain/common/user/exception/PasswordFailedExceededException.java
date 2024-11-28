package io.github.patternhelloworld.tak.domain.common.user.exception;


import io.github.patternhelloworld.common.config.response.error.message.GeneralExceptionMessage;
import lombok.Getter;


@Getter
public class PasswordFailedExceededException extends RuntimeException {

    private GeneralExceptionMessage generalExceptionMessage;

    public PasswordFailedExceededException() {
        this.generalExceptionMessage = GeneralExceptionMessage.PASSWORD_FAILED_EXCEEDED;
    }
}
