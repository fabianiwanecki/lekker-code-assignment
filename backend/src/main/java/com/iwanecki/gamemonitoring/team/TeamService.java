package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamDto createTeam(CreateTeamReqDto createTeamReq) {
        TeamEntity team = new TeamEntity()
                .setName(createTeamReq.name())
                .setMaxMembers(createTeamReq.maxMembers());

        team = teamRepository.save(team);

        userService.addUserToTeam(createTeamReq.userUuid(), TeamRole.OWNER, team);

        return teamMapper.mapEntitytoDto(team);
    }

    @Transactional
    public void deleteTeam(UUID uuid) {
        if (teamRepository.findById(uuid).isEmpty()) {
            throw new TeamNotFoundException();
        }

        userService.removeTeam(uuid);
        teamRepository.deleteById(uuid);
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

    public PageDto<TeamDto> listTeams(Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        Page<TeamEntity> teams = teamRepository.findAll(pageable);

        return new PageDto<>(page, teams.getContent().size(), teams.getTotalElements(), teamMapper.mapEntitytoDto(teams.getContent()));
    }

    public TeamWithMembersDto fetchTeamDetails(UUID uuid) {
        TeamEntity team = teamRepository.findById(uuid).orElseThrow(TeamNotFoundException::new);

        return teamMapper.mapEntitytoDtoWithMembers(team);
    }
}
