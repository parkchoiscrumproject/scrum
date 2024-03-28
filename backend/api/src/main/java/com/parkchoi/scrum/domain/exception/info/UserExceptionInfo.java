package com.parkchoi.scrum.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptionInfo {
    NOT_FOUNT_USER(HttpStatus.NOT_FOUND, "1000", "사용자를 찾을 수 없습니다.");



    private HttpStatus status;
    private String code;
    private String message;

    UserExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
