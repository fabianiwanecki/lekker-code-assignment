package com.iwanecki.gamemonitoring.team;

import java.util.UUID;

public record TeamDto(UUID uuid, String name, Integer maxMembers) {
}
