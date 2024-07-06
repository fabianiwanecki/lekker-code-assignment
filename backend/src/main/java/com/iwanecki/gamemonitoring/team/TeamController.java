package com.iwanecki.gamemonitoring.team;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID uuid) {
        teamService.deleteTeam(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
