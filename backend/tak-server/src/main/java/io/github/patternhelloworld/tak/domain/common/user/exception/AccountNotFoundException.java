package io.github.patternhelloworld.tak.domain.common.user.exception;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private long userIdx;
    private String userId;

    public AccountNotFoundException(long userIdx) {
        this.userIdx = userIdx;
    }

    public AccountNotFoundException(String userId) {
        this.userId = userId;
    }

}
