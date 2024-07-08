package com.iwanecki.gamemonitoring.team;

public class TeamRequestNotFoundException extends RuntimeException {
    public TeamRequestNotFoundException(String message) {
        super(message);
    }
}
