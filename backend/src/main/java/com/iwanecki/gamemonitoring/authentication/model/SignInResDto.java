package com.iwanecki.gamemonitoring.authentication.model;

import java.util.UUID;

public record SignInResDto(String token, UUID userUuid) {
}
