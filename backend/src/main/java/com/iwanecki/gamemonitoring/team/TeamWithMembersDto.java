package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserDto;

import java.util.List;
import java.util.UUID;

public record TeamWithMembersDto(UUID uuid, String name, Integer maxMembers, List<UserDto> members) {
}
