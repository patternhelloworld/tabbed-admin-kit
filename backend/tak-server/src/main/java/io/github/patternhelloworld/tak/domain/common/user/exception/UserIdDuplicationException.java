package io.github.patternhelloworld.tak.domain.common.user.exception;


import lombok.Getter;

@Getter
public class UserIdDuplicationException extends RuntimeException {

    private String userId;
    private String field;

  public UserIdDuplicationException(String userId) {
        this.field = "userId";
        this.userId = userId;
    }
}
