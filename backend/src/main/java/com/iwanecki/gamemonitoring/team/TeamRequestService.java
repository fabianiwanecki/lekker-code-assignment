package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;
    private final UserService userService;
    private final TeamService teamService;

    public void createTeamRequest(UUID teamUuid, String username) {
        UserDto user = userService.fetchByUsername(username);

        if (user.team() != null) {
            throw new AlreadyTeamMemberException("User is already member of a team");
        }

        if (teamRequestRepository.existsById(user.uuid())) {
            throw new TeamRequestIsPendingException("User already has a pending request for a team");
        }

        TeamRequestEntity teamRequest = new TeamRequestEntity()
                .setTeamUuid(teamUuid)
                .setUserUuid(user.uuid());

        teamRequestRepository.save(teamRequest);
    }

    @Transactional
    public void answerTeamRequest(UUID teamUuid, AnswerTeamRequestReqDto answerTeamRequestReq) {
        if (teamRequestRepository.findFirstByUserUuidAndTeamUuid(answerTeamRequestReq.userUuid(), teamUuid).isEmpty()) {
            throw new TeamRequestNotFoundException("The user doesn't have a pending request for this team");
        }

        if (answerTeamRequestReq.acceptRequest()) {
            teamService.addUserToTeam(answerTeamRequestReq.userUuid(), TeamRole.MEMBER, teamUuid);
        }

        teamRequestRepository.deleteById(answerTeamRequestReq.userUuid());
    }

    public void deleteTeamRequest(UUID teamUuid, UUID userUuid) {
        if (!userService.existsByUuid(userUuid)) {
            throw new UserNotFoundException();
        }

        if (teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid).isEmpty()) {
            throw new TeamRequestNotFoundException("The user doesn't have a pending request for this team");
        }

        teamRequestRepository.deleteById(userUuid);
    }

    public void deleteAllRequests(UUID teamUuid) {
        teamRequestRepository.deleteAllByTeamUuid(teamUuid);
    }

    public List<TeamRequestDto> listTeamRequest(UUID teamUuid) {
        List<TeamRequestEntity> teamRequest = teamRequestRepository.findAllByTeamUuid(teamUuid);
        List<UUID> userUuidList = teamRequest.stream().map(TeamRequestEntity::getUserUuid).toList();
        List<UserWithRankDto> userWithRankList = userService.fetchMultipleUsersWithRank(userUuidList);

        return userWithRankList.stream().map(TeamRequestDto::new).toList();
    }
}
