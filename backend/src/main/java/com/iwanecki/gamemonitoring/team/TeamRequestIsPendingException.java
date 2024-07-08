package com.iwanecki.gamemonitoring.team;

public class TeamRequestIsPendingException extends RuntimeException {
    public TeamRequestIsPendingException(String message) {
        super(message);
    }
}
