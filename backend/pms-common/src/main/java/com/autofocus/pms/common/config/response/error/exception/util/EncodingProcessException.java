package com.autofocus.pms.common.config.response.error.exception.util;

import com.autofocus.pms.common.config.response.error.message.ExceptionMessageInterface;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EncodingProcessException extends RuntimeException {

    private ExceptionMessageInterface exceptionMessage;
    private String value;
    private String message;

    public ExceptionMessageInterface getExceptionMessage() {
        return exceptionMessage;
    }

    public String getValue() {
        return value;
    }

    public EncodingProcessException(ExceptionMessageInterface exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
        this.value = "";
    }

    public EncodingProcessException(ExceptionMessageInterface exceptionMessage, String value) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
        this.value = value;
    }

    public EncodingProcessException(String message) {
        super(message);
        this.message = message;
    }

}
