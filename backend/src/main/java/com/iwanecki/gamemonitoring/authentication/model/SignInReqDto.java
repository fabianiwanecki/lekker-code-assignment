package com.iwanecki.gamemonitoring.authentication.model;

import jakarta.validation.constraints.NotNull;

public record SignInReqDto(
        @NotNull String username,
        @NotNull String password) {
}
