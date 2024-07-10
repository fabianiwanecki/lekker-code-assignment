package com.iwanecki.gamemonitoring.team.model;

import com.iwanecki.gamemonitoring.user.model.UserWithRankDto;

import java.util.List;
import java.util.UUID;

public record TeamDetailedWithMembersDto(UUID uuid, String name, Integer maxMembers, Integer currentMembers, String owner, Long totalScore, List<UserWithRankDto> members) {
}
