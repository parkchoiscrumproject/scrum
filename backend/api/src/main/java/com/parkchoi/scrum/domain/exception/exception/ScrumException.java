package com.parkchoi.scrum.domain.exception.exception;

import com.parkchoi.scrum.domain.exception.info.ScrumExceptionInfo;
import lombok.Getter;

@Getter
public class ScrumException extends RuntimeException{
    private ScrumExceptionInfo exception;
    private String log;

    public ScrumException(ScrumExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
