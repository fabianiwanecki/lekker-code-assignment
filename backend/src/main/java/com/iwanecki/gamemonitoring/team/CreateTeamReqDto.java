package com.iwanecki.gamemonitoring.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTeamReqDto(@NotNull UUID userUuid, @NotNull @NotEmpty String name, @Min(10) Integer maxMembers) {
}
