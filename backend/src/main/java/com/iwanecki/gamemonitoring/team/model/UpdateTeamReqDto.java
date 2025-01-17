package com.iwanecki.gamemonitoring.team.model;

import com.iwanecki.gamemonitoring.shared.validation.NullOrNotBlank;
import jakarta.validation.constraints.Min;

public record UpdateTeamReqDto(@NullOrNotBlank String name, @Min(10) Integer maxMembers) {
}
