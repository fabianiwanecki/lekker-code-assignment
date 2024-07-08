package com.iwanecki.gamemonitoring.team;

import java.util.UUID;

public record TeamDetailedDto(UUID uuid, String name, Integer maxMembers, Integer currentMembers, String owner, Long totalScore) {
}
