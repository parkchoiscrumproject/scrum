package com.parkchoi.scrum.domain.team.exception;

public class TeamNotFoundException extends IllegalArgumentException{
    public TeamNotFoundException(String message) {
        super(message);
    }
}
