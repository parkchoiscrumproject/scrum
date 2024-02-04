package com.parkchoi.scrum.domain.user.exception;

public class AuthFailException extends IllegalArgumentException{
    public AuthFailException(String message) {
        super(message);
    }
}
