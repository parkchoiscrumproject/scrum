package com.parkchoi.scrum.domain.exception.handler;

import com.parkchoi.scrum.domain.exception.exception.ScrumException;
import com.parkchoi.scrum.domain.exception.exception.UserException;
import com.parkchoi.scrum.domain.team.exception.*;
import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.util.api.ApiResponse;
import com.parkchoi.scrum.util.s3.ExtensionErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("유효성 검사 실패 예외 발생");
        AtomicInteger index = new AtomicInteger(1);

        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(error -> index.getAndIncrement() + ". " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }

    // Validated 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e){
        log.error("유효성 검사 실패 예외 발생");
        AtomicInteger index = new AtomicInteger(1);

        String errorMessage = e.getConstraintViolations().stream()
                .map(error -> index.getAndIncrement() + ". " + error.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }

    // 유저 예외처리
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e ,HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}", request.getRequestURI(), e.getException().getMessage());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 스크럼 예외처리
    @ExceptionHandler(ScrumException.class)
    public ResponseEntity<ApiResponse<Void>> handleScrumException(ScrumException e, HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}", request.getRequestURI(), e.getException().getMessage());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 인증 실패
    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthFailException(AuthFailException e, HttpServletRequest request) {
        log.warn("인증 실패 - 요청 경로 : {}, 이유 {}", request.getRequestURI(), e.getMessage());
        
        return ResponseEntity.status(401).body(ApiResponse.createClientError("인증 정보가 유효하지 않습니다. 다시 로그인해 주세요"));
    }

    // 사진 타입 에러
    @ExceptionHandler(ExtensionErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleExtensionErrorException(ExtensionErrorException e) {
        log.error("사진 타입 에러");
        return ResponseEntity.status(415).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 없음
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTeamNotFoundException(TeamNotFoundException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(404).body(ApiResponse.createClientError("문제가 발생했습니다. 새로고침 후 다시 시도해주세요."));
    }

    // 특정 유저가 팀에 속하지 않음
    @ExceptionHandler(NonParticipantUserException.class)
    public ResponseEntity<ApiResponse<Void>> handleNonParticipantUserException(NonParticipantUserException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(404).body(ApiResponse.createClientError("문제가 발생했습니다. 새로고침 후 다시 시도해주세요."));
    }

    // 팀 생성 실패
    @ExceptionHandler(FailCreateTeamException.class)
    public ResponseEntity<ApiResponse<Void>> handleFailCreateTeamException(FailCreateTeamException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(500).body(ApiResponse.createServerError("서버에 문제가 발생했습니다."));
    }

    // 현재 유저가 팀의 리더가 아님
    @ExceptionHandler(NoTeamLeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoTeamLeaderException(NoTeamLeaderException e){
        log.error("현재 유저가 팀의 리더가 아님");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 팀에 속해있다면
    @ExceptionHandler(AlreadyTeamMemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyTeamMemberException(AlreadyTeamMemberException e){
        log.error("이미 팀에 속해 있는 상태");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 팀 초대 목록에 있다면
    @ExceptionHandler(AlreadyInviteMemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyInviteMemberException(AlreadyInviteMemberException e){
        log.error("이미 팀에 초대된 상태");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 초대가 없다면
    @ExceptionHandler(InviteNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInviteNotFoundException(InviteNotFoundException e){
        log.error("초대 목록에 존재하지 않음");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }


}
