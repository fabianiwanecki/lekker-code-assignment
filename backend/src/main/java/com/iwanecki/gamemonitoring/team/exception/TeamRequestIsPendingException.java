package com.iwanecki.gamemonitoring.team.exception;

public class TeamRequestIsPendingException extends RuntimeException {
    public TeamRequestIsPendingException(String message) {
        super(message);
    }
}
