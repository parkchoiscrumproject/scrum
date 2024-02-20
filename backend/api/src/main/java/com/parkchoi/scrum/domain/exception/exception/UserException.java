package com.parkchoi.scrum.domain.exception.exception;

import com.parkchoi.scrum.domain.exception.info.UserExceptionInfo;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException{
    private UserExceptionInfo exception;
    private String log;

    public UserException(UserExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
