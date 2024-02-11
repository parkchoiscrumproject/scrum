package com.parkchoi.scrum.domain.exception.controller;

import com.parkchoi.scrum.domain.scrum.exception.*;
import com.parkchoi.scrum.domain.team.exception.*;
import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.util.api.ApiResponse;
import com.parkchoi.scrum.util.s3.ExtensionErrorException;
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

    // 유저 없음
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
        log.error("유저 없음");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 인증 실패
    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthFailException(AuthFailException e) {
        log.error("인증 실패");
        return ResponseEntity.status(401).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 사진 타입 에러
    @ExceptionHandler(ExtensionErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleExtensionErrorException(ExtensionErrorException e) {
        log.error("사진 타입 에러");
        return ResponseEntity.status(415).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 없음
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTeamNotFoundException(TeamNotFoundException e) {
        log.error("팀 없음");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 특정 유저가 팀에 속하지 않음
    @ExceptionHandler(NonParticipantUserException.class)
    public ResponseEntity<ApiResponse<Void>> handleNonParticipantUserException(NonParticipantUserException e) {
        log.error("해당 유저가 팀에 속하지 않음");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 팀 생성 실패
    @ExceptionHandler(FailCreateTeamException.class)
    public ResponseEntity<ApiResponse<Void>> handleFailCreateTeamException(FailCreateTeamException e) {
        log.error("팀 생성 실패");
        return ResponseEntity.status(500).body(ApiResponse.createServerError(e.getMessage()));
    }

    // 스크럼 없음
    @ExceptionHandler(ScrumNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleScrumNotFoundException(ScrumNotFoundException e) {
        log.error("스크럼 없음");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 멤버가 꽉 찬 스크럼
    @ExceptionHandler(MaxMemberScrumException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxMemberScrumException(MaxMemberScrumException e) {
        log.error("스크럼 최대 멤버 초과");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 현재 유저가 스크럼의 리더가 아님
    @ExceptionHandler(NotScrumLeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotScrumLeaderException(NotScrumLeaderException e) {
        log.error("현재 유저가 스크럼의 리더가 아님");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 스크럼이 시작 상태
    @ExceptionHandler(AlreadyScrumStartException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyScrumStartException(AlreadyScrumStartException e) {
        log.error("이미 스크럼이 시작 상태");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 스크럼이 종료 상태
    @ExceptionHandler(AlreadyScrumEndException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyScrumEndException(AlreadyScrumEndException e) {
        log.error("이미 스크럼이 종료 상태");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 아직 스크럼이 시작하지 않은 상태
    @ExceptionHandler(NotStartScrumException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotStartScrumException(NotStartScrumException e) {
        log.error("아직 스크럼이 시작하지 않은 상태");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 스크럼에 참여중이면
    @ExceptionHandler(AlreadyScrumEnterException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyScrumEnterException(AlreadyScrumEnterException e) {
        log.error("이미 스크럼에 참여중");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
    }

    // 이미 스크럼이 삭제됐다면
    @ExceptionHandler(AlreadyScrumRemoveException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyScrumRemoveException(AlreadyScrumRemoveException e) {
        log.error("이미 삭제된 스크럼");
        return ResponseEntity.status(404).body(ApiResponse.createClientError(e.getMessage()));
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
