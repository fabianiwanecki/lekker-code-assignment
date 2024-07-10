package com.iwanecki.gamemonitoring.user.model;

import java.util.UUID;

public record UserWithRankDto(UUID uuid, String username, Integer score, Long rank) {
}
