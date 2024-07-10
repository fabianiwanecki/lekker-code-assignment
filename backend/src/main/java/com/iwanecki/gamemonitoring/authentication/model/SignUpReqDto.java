package com.iwanecki.gamemonitoring.authentication.model;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SignUpReqDto(
        @NotNull @Length(min = 5, max = 30) String username,
        @NotNull @Length(min = 8, max = 50) String password) {
}
