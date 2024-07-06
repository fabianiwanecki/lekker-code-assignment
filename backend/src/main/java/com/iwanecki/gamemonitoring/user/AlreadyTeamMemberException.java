package com.iwanecki.gamemonitoring.user;

public class AlreadyTeamMemberException extends RuntimeException {

    public AlreadyTeamMemberException(String message) {
        super(message);
    }
}
