package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
            throw new TeamNotFoundException("Team not found");
        }

        userService.removeTeam(uuid);
        teamRepository.deleteById(uuid);
    }
}
