package com.parkchoi.scrum.domain.exception.controller;

import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.util.api.ApiResponse;
import com.parkchoi.scrum.util.s3.ExtensionErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponse<?> handleUserNotFoundException(UserNotFoundException e) {
        return ApiResponse.createError(e.getMessage());
    }

    @ExceptionHandler(AuthFailException.class)
    public ApiResponse<?> handleAuthFailException(AuthFailException e) {
        return ApiResponse.createError(e.getMessage());
    }

    @ExceptionHandler(ExtensionErrorException.class)
    public ApiResponse<?> handleExtensionErrorException(ExtensionErrorException e) {
        return ApiResponse.createError(e.getMessage());
    }
}
