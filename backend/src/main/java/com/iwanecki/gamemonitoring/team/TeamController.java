package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @PutMapping("{uuid}")
    public TeamDto updateTeam(@PathVariable UUID uuid, @Valid @NotNull @RequestBody UpdateTeamReqDto updateTeamReq) {
        return teamService.updateTeam(uuid, updateTeamReq);
    }

    @GetMapping("{uuid}")
    public TeamWithMembersDto fetchTeamDetails(@PathVariable UUID uuid) {
        return teamService.fetchTeamDetails(uuid);
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID uuid) {
        teamService.deleteTeam(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public PageDto<TeamDto> listTeams(@Min(1) @RequestParam Integer page, @Min(1) @Max(100) @RequestParam Integer size) {
        return teamService.listTeams(page, size);
    }

}
