package com.iwanecki.gamemonitoring.user.model;

import com.iwanecki.gamemonitoring.team.TeamDto;

import java.util.UUID;

public record UserDto(UUID uuid, String username, Integer score, TeamDto team) {
}
