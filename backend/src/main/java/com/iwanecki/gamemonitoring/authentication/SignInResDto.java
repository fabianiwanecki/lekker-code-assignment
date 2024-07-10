package com.iwanecki.gamemonitoring.authentication;

import java.util.UUID;

public record SignInResDto(String token, UUID userUuid) {
}
