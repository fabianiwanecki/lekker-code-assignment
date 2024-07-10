package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.team.exception.TeamNotFoundException;
import com.iwanecki.gamemonitoring.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTeamService {

    private final TeamService teamService;
    private final UserService userService;
    private final TeamRequestService teamRequestService;

    @Transactional
    public void deleteTeam(UUID uuid) {
        if (!teamService.existsByUuid(uuid)) {
            throw new TeamNotFoundException();
        }

        teamRequestService.deleteAllRequests(uuid);
        userService.removeUsersFromTeam(uuid);
        teamService.deleteByUuid(uuid);
    }
}
