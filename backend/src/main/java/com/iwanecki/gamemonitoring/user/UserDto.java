package com.iwanecki.gamemonitoring.user;

import java.util.UUID;

public record UserDto(UUID uuid, String username, Integer score) {
}
