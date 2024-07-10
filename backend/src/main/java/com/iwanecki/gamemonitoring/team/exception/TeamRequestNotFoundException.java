package com.iwanecki.gamemonitoring.team.exception;

public class TeamRequestNotFoundException extends RuntimeException {
    public TeamRequestNotFoundException(String message) {
        super(message);
    }
}
