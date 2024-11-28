package io.github.patternhelloworld.common.config.response.error.exception.file;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class FileNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
}
