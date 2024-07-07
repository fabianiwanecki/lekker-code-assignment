package com.iwanecki.gamemonitoring.team;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        super("Team not found");
    }
}
