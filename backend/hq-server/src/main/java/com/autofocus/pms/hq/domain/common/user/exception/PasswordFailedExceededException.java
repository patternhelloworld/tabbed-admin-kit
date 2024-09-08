package com.autofocus.pms.hq.domain.common.user.exception;


import com.autofocus.pms.common.config.response.error.message.GeneralExceptionMessage;
import lombok.Getter;


@Getter
public class PasswordFailedExceededException extends RuntimeException {

    private GeneralExceptionMessage generalExceptionMessage;

    public PasswordFailedExceededException() {
        this.generalExceptionMessage = GeneralExceptionMessage.PASSWORD_FAILED_EXCEEDED;
    }
}
