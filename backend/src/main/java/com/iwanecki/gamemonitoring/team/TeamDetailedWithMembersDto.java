package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserWithRankDto;

import java.util.List;
import java.util.UUID;

public record TeamDetailedWithMembersDto(UUID uuid, String name, Integer maxMembers, Integer currentMembers, String owner, Long totalScore, List<UserWithRankDto> members) {
}
