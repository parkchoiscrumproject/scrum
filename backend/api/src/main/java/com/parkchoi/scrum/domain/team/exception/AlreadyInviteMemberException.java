package com.parkchoi.scrum.domain.team.exception;

public class AlreadyInviteMemberException extends RuntimeException{
    AlreadyInviteMemberException(String message){
        super(message);
    }
}
