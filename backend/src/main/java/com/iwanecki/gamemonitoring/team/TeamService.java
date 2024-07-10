package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.exception.TeamNotFoundException;
import com.iwanecki.gamemonitoring.team.exception.TeamUpdateException;
import com.iwanecki.gamemonitoring.team.model.CreateTeamReqDto;
import com.iwanecki.gamemonitoring.team.model.TeamDetailedDto;
import com.iwanecki.gamemonitoring.team.model.TeamDetailedWithMembersDto;
import com.iwanecki.gamemonitoring.team.model.UpdateTeamReqDto;
import com.iwanecki.gamemonitoring.user.*;
import com.iwanecki.gamemonitoring.user.model.UserWithRankDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamDetailedRepository teamDetailedRepository;
    private final UserService userService;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamDto createTeam(CreateTeamReqDto createTeamReq, String username) {
        TeamEntity team = new TeamEntity()
                .setName(createTeamReq.name())
                .setMaxMembers(createTeamReq.maxMembers());

        team = teamRepository.save(team);

        addUserToTeam(username, TeamRole.OWNER, team);

        return teamMapper.mapEntitytoDto(team);
    }


    public TeamDto updateTeam(UUID uuid, UpdateTeamReqDto updateTeamReq) {
        TeamEntity team = teamRepository.findById(uuid).orElseThrow(TeamNotFoundException::new);

        if (updateTeamReq.maxMembers() != null) {
            if (team.getMembers().size() > updateTeamReq.maxMembers()) {
                throw new TeamUpdateException("Cannot reduce max members below the current number of members");
            }
            team.setMaxMembers(updateTeamReq.maxMembers());
        }

        if (updateTeamReq.name() != null) {
            team.setName(updateTeamReq.name());
        }

        team = teamRepository.save(team);

        return teamMapper.mapEntitytoDto(team);
    }

    public PageDto<TeamDetailedDto> listTeams(Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        Page<TeamDetailedEntity> teams = teamDetailedRepository.findAll(pageable);
        return new PageDto<>(page, teams.getContent().size(), teams.getTotalElements(), teamMapper.mapEntitytoDtoDetailed(teams.getContent()));
    }

    public TeamDetailedWithMembersDto fetchTeamDetails(UUID uuid) {
        TeamDetailedEntity team = teamDetailedRepository.findById(uuid).orElseThrow(TeamNotFoundException::new);
        List<UserWithRankDto> users = userService.fetchMultipleUsersWithRank(team.getMembers().stream().map(UserEntity::getUuid).toList());
        return teamMapper.mapEntitytoDtoWithMembers(team, users);
    }

    private TeamEntity fetchByUuid(UUID teamUuid) {
        return teamRepository.findById(teamUuid).orElseThrow(TeamNotFoundException::new);
    }

    public void addUserToTeam(String username, TeamRole teamRole, TeamEntity team) {
        userService.addUserToTeam(username, team, teamRole);
    }

    public void addUserToTeam(UUID userUuid, TeamRole teamRole, UUID teamUuid) {
        TeamEntity team = fetchByUuid(teamUuid);
        userService.addUserToTeam(userUuid, team, teamRole);
    }

    public boolean existsByUuid(UUID uuid) {
        return teamRepository.existsById(uuid);
    }

    public void deleteByUuid(UUID uuid) {
        teamRepository.deleteById(uuid);
    }
}
