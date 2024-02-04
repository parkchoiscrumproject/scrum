package com.parkchoi.scrum.domain.team.exception;

public class AlreadyTeamMemberException extends RuntimeException{
    public AlreadyTeamMemberException(String message){
        super(message);
    }
}
