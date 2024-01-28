package com.parkchoi.scrum.domain.team.exception;

public class NoTeamLeaderException extends RuntimeException{
    public NoTeamLeaderException(String message) {
        super(message);
    }
}
