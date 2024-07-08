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
    private final UserRepository userRepository;
    private final UserService userService;
    private final TeamRepository teamRepository;

    public void createTeamRequest(UUID teamUuid, String username) {
        UserEntity user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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
            TeamEntity team = teamRepository.findById(teamUuid).orElseThrow(TeamNotFoundException::new);
            userService.addUserToTeam(answerTeamRequestReq.userUuid(), TeamRole.MEMBER, team);
        }

        teamRequestRepository.deleteById(answerTeamRequestReq.userUuid());
    }

    public void deleteTeamRequest(UUID teamUuid, String username) {
        UserEntity user = userRepository.findFirstByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (teamRequestRepository.findFirstByUserUuidAndTeamUuid(user.getUuid(), teamUuid).isEmpty()) {
            throw new TeamRequestNotFoundException("The user doesn't have a pending request for this team");
        }

        teamRequestRepository.deleteById(user.getUuid());
    }

    public void deleteAllRequests(UUID teamUuid) {
        teamRequestRepository.deleteAllByTeamUuid(teamUuid);
    }
}
