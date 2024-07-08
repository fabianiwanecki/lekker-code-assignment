package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;
    private final UserService userService;

    public void createTeamRequest(UUID teamUuid, String username) {
        UserEntity user = userService.fetchByUsername(username);

        if (user.getTeam() != null) {
            throw new AlreadyTeamMemberException("User is already member of a team");
        }

        if (teamRequestRepository.existsById(user.getUuid())) {
            throw new TeamRequestIsPendingException("User already has a pending request for a team");
        }

        TeamRequestEntity teamRequest = new TeamRequestEntity()
                .setTeamUuid(teamUuid)
                .setUserUuid(user.getUuid());

        teamRequestRepository.save(teamRequest);
    }

    @Transactional
    public void answerTeamRequest(UUID teamUuid, AnswerTeamRequestReqDto answerTeamRequestReq) {
        if (teamRequestRepository.findFirstByUserUuidAndTeamUuid(answerTeamRequestReq.userUuid(), teamUuid).isEmpty()) {
            throw new TeamRequestNotFoundException("The user doesn't have a pending request for this team");
        }

        if (answerTeamRequestReq.acceptRequest()) {
            userService.addUserToTeam(answerTeamRequestReq.userUuid(), TeamRole.MEMBER, teamUuid);
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
}
