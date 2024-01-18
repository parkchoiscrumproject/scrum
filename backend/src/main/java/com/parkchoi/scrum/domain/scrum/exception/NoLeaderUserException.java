package com.parkchoi.scrum.domain.scrum.exception;

public class NoLeaderUserException extends RuntimeException{
    public NoLeaderUserException(String message) {
        super(message);
    }
}
