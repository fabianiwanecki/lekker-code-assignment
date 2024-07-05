package com.iwanecki.gamemonitoring.authentication;

import java.util.UUID;

public record UserDto(UUID uuid, String username) {
}
