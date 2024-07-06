package com.iwanecki.gamemonitoring.team;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public TeamDto createTeam(@Valid @NotNull @RequestBody CreateTeamReqDto createTeamReq) {
        return teamService.createTeam(createTeamReq);
    }

}
