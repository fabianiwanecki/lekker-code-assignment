package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.team.model.CreateTeamReqDto;
import com.iwanecki.gamemonitoring.user.model.UserDto;
import com.iwanecki.gamemonitoring.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateTeamService {

    private final TeamService teamService;
    private final TeamRequestService teamRequestService;
    private final UserService userService;


    @Transactional
    public TeamDto createTeam(CreateTeamReqDto createTeamReq, String username) {
        TeamDto team = teamService.createTeam(createTeamReq, username);
        UserDto user = userService.fetchByUsername(username);
        teamRequestService.deleteTeamRequestForUser(user.uuid());
        return team;
    }
}
