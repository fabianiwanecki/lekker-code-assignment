package com.iwanecki.gamemonitoring.team.exception;

public class TeamAlreadyFullException extends RuntimeException {
    public TeamAlreadyFullException(String message) {
        super(message);
    }
}
