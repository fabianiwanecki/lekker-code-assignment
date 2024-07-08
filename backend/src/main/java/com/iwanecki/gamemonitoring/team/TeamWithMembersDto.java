package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserWithRankDto;

import java.util.List;
import java.util.UUID;

public record TeamWithMembersDto(UUID uuid, String name, Integer maxMembers, List<UserWithRankDto> members) {
}
