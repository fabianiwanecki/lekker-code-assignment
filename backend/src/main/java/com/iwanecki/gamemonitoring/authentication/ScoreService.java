package com.iwanecki.gamemonitoring.authentication;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScoreService {

    private final Random random;

    public ScoreService() {
        this.random = new Random();
    }

    public Integer generateRandomScore() {
        return random.nextInt(10000);
    }
}
