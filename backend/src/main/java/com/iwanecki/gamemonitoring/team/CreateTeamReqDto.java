package com.iwanecki.gamemonitoring.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateTeamReqDto(@NotNull @Length(min = 5, max = 30) String name, @NotNull @Min(10) Integer maxMembers) {
}
