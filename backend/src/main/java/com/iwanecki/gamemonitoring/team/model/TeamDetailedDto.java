package com.iwanecki.gamemonitoring.team.model;

import java.util.UUID;

public record TeamDetailedDto(UUID uuid, String name, Integer maxMembers, Integer currentMembers, String owner, Long totalScore) {
}
