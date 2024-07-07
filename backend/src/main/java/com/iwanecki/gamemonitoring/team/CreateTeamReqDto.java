package com.iwanecki.gamemonitoring.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTeamReqDto(@NotNull UUID userUuid, @NotNull @NotBlank String name, @NotNull @Min(10) Integer maxMembers) {
}
