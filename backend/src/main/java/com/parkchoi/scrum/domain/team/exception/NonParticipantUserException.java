package com.parkchoi.scrum.domain.team.exception;

public class NonParticipantUserException extends RuntimeException{
    public NonParticipantUserException(String message) {
        super(message);
    }
}
