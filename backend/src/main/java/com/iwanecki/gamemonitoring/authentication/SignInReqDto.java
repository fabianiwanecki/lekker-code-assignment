package com.iwanecki.gamemonitoring.authentication;

import jakarta.validation.constraints.NotNull;

public record SignInReqDto(
        @NotNull String username,
        @NotNull String password) {
}
