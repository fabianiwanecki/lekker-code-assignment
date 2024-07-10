package com.iwanecki.gamemonitoring.team.model;

import com.iwanecki.gamemonitoring.user.model.UserWithRankDto;

public record TeamRequestDto(UserWithRankDto user) {
}
