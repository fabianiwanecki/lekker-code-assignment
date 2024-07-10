package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("teams")
public class TeamController {

    private final TeamService teamService;
    private final DeleteTeamService deleteTeamService;
    private final CreateTeamService createTeamService;
    private final TeamRequestService teamRequestService;

    @PostMapping
    public TeamDto createTeam(@Valid @NotNull @RequestBody CreateTeamReqDto createTeamReq, Authentication authentication) {
        return createTeamService.createTeam(createTeamReq, authentication.getName());
    }

    @PreAuthorize("hasAuthority('OWNER_' + #uuid)")
    @PutMapping("{uuid}")
    public TeamDto updateTeam(@PathVariable UUID uuid, @Valid @NotNull @RequestBody UpdateTeamReqDto updateTeamReq) {
        return teamService.updateTeam(uuid, updateTeamReq);
    }

    @GetMapping("{uuid}")
    public TeamDetailedWithMembersDto fetchTeamDetails(@PathVariable UUID uuid) {
        return teamService.fetchTeamDetails(uuid);
    }

    @PreAuthorize("hasAuthority('OWNER_' + #uuid)")
    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID uuid) {
        deleteTeamService.deleteTeam(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public PageDto<TeamDetailedDto> listTeams(@Min(1) @RequestParam Integer page, @Min(1) @Max(100) @RequestParam Integer size) {
        return teamService.listTeams(page, size);
    }

    @PostMapping("{uuid}/requests")
    public ResponseEntity<Void> createTeamRequest(@PathVariable UUID uuid, Authentication authentication) {
        teamRequestService.createTeamRequest(uuid, authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('OWNER_' + #teamUuid)")
    @PutMapping("{teamUuid}/requests")
    public ResponseEntity<Void> answerTeamRequest(@PathVariable UUID teamUuid, @Valid @RequestBody AnswerTeamRequestReqDto answerTeamRequestReq) {
        teamRequestService.answerTeamRequest(teamUuid, answerTeamRequestReq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{teamUuid}/requests")
    public ResponseEntity<Void> deleteTeamRequest(@PathVariable UUID teamUuid, Authentication authentication) {
        teamRequestService.deleteTeamRequest(teamUuid, UUID.fromString(authentication.getName()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{teamUuid}/requests")
    public List<TeamRequestDto> listTeamRequests(@PathVariable UUID teamUuid) {
        return teamRequestService.listTeamRequest(teamUuid);
    }
}
