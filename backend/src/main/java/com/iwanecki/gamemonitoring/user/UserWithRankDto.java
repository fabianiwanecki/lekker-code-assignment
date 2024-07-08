package com.iwanecki.gamemonitoring.user;

import java.util.UUID;

public record UserWithRankDto(UUID uuid, String username, Integer score, Long rank) {
}
