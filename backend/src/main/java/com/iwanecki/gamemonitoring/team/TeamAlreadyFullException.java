package com.iwanecki.gamemonitoring.team;

public class TeamAlreadyFullException extends RuntimeException {
    public TeamAlreadyFullException(String message) {
        super(message);
    }
}
