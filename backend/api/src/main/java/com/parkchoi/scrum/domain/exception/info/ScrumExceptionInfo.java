package com.parkchoi.scrum.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ScrumExceptionInfo {
    SCRUM_NOT_FOUND(HttpStatus.NOT_FOUND, "2000", "스크럼을 찾을 수 없습니다."),
    SCRUM_NOT_STARTED(HttpStatus.BAD_REQUEST, "2001", "시작되지 않은 스크럼입니다."),
    NOT_SCRUM_LEADER(HttpStatus.BAD_REQUEST, "2002", "스크럼의 리더만 가능합니다."),
    SCRUM_MEMBER_MAXED(HttpStatus.BAD_REQUEST, "2003", "스크럼의 멤버가 최대입니다."),
    SCRUM_CREATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "2004" ,"스크럼 생성에 실패했습니다."),
    SCRUM_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "2005", "이미 시작된 스크럼입니다."),
    SCRUM_ALREADY_REMOVED(HttpStatus.BAD_REQUEST, "2006", "이미 삭제된 스크럼입니다."),
    SCRUM_ALREADY_ENTERED(HttpStatus.BAD_REQUEST, "2007", "이미 참여중인 스크럼입니다."),
    SCRUM_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "2008", "이미 종료된 스크럼입니다.");

    private HttpStatus status;
    private String code;
    private String message;

    ScrumExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
