package com.iwanecki.gamemonitoring.team.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AnswerTeamRequestReqDto(@NotNull UUID userUuid, @NotNull Boolean acceptRequest) {
}
