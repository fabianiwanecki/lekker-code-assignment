package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.team.TeamDto;

import java.util.UUID;

public record UserWithRankAndTeamDto(UUID uuid, String username, Integer score, Long rank, TeamDto team) {
}
