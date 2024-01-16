package com.parkchoi.scrum.domain.exception.controller;

import com.parkchoi.scrum.domain.team.exception.FailCreateTeamException;
import com.parkchoi.scrum.domain.team.exception.NonParticipantUserException;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.util.api.ApiResponse;
import com.parkchoi.scrum.util.s3.ExtensionErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유저 없음
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 인증 실패
    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthFailException(AuthFailException e) {
        return ResponseEntity.status(401).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 사진 타입 에러
    @ExceptionHandler(ExtensionErrorException.class)
    public ResponseEntity<ApiResponse<?>> handleExtensionErrorException(ExtensionErrorException e) {
        return ResponseEntity.status(415).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 없음
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleTeamNotFoundException(TeamNotFoundException e) {
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 특정 유저가 팀에 속하지 않음
    @ExceptionHandler(NonParticipantUserException.class)
    public ResponseEntity<ApiResponse<?>> handleNonParticipantUserException(NonParticipantUserException e) {
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 생성 실패
    @ExceptionHandler(FailCreateTeamException.class)
    public ResponseEntity<ApiResponse<?>> handleFailCreateTeamException(FailCreateTeamException e) {
        return ResponseEntity.status(500).body(ApiResponse.createServerError(e.getMessage()));
    }
}
