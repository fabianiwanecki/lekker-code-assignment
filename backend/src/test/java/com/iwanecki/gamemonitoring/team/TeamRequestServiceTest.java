package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamRequestServiceTest {

    @InjectMocks
    private TeamRequestService teamRequestService;

    @Mock
    private TeamRequestRepository teamRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private TeamRepository teamRepository;

    @Nested
    class CreateTeamRequestTest {

        @Test
        void createTeamRequest_WithUserAlreadyHasATeam_ShouldThrow() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            when(userService.fetchByUsername("TestUser")).thenReturn(new UserEntity().setTeam(new TeamEntity()));

            assertThrows(AlreadyTeamMemberException.class,
                    () -> teamRequestService.createTeamRequest(teamUuid, "TestUser"));
        }

        @Test
        void createTeamRequest_WithUserAlreadyHasAPendingRequest_ShouldThrow() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(userService.fetchByUsername("TestUser")).thenReturn(new UserEntity().setUuid(userUuid));
            when(teamRequestRepository.existsById(userUuid)).thenReturn(true);

            assertThrows(TeamRequestIsPendingException.class,
                    () -> teamRequestService.createTeamRequest(teamUuid, "TestUser"));
        }

        @Test
        void createTeamRequest_WithValidParams_ShouldCreateRequest() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(userService.fetchByUsername("TestUser")).thenReturn(new UserEntity().setUuid(userUuid));
            when(teamRequestRepository.existsById(userUuid)).thenReturn(false);

            teamRequestService.createTeamRequest(teamUuid, "TestUser");

            verify(teamRequestRepository).save(any());
        }
    }

    @Nested
    class DeleteTeamRequestTest {

        @Test
        void deleteTeamRequest_WithUserNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("67a0981f-c13a-4972-878c-3da1e28ef82c");
            when(userService.existsByUuid(userUuid)).thenReturn(false);

            assertThrows(UserNotFoundException.class, () ->
                    teamRequestService.deleteTeamRequest(teamUuid, userUuid));
        }

        @Test
        void deleteTeamRequest_WithNoPendingRequest_ShouldThrow() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(userService.existsByUuid(userUuid)).thenReturn(true);
            when(teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid)).thenReturn(Optional.empty());

            assertThrows(TeamRequestNotFoundException.class, () ->
                    teamRequestService.deleteTeamRequest(teamUuid, userUuid));
        }

        @Test
        void deleteTeamRequest_WithValidParams_ShouldDeleteRequest() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(userService.existsByUuid(userUuid)).thenReturn(true);
            when(teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid)).thenReturn(Optional.of(new TeamRequestEntity()));

            teamRequestService.deleteTeamRequest(teamUuid, userUuid);

            verify(teamRequestRepository).deleteById(userUuid);
        }
    }

    @Nested
    class AnswerTeamRequestTest {

        @Test
        void answerTeamRequest_WithNoRequestFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid)).thenReturn(Optional.empty());

            AnswerTeamRequestReqDto answerTeamRequestReq = new AnswerTeamRequestReqDto(userUuid, true);
            assertThrows(TeamRequestNotFoundException.class, () ->
                    teamRequestService.answerTeamRequest(teamUuid, answerTeamRequestReq));
        }

        @Test
        void answerTeamRequest_WithAcceptRequest_ShouldAddUserToTeamAsMember() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            when(teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid)).thenReturn(Optional.of(new TeamRequestEntity()));

            teamRequestService.answerTeamRequest(teamUuid, new AnswerTeamRequestReqDto(userUuid, true));

            verify(userService).addUserToTeam(userUuid, TeamRole.MEMBER, teamUuid);
            verify(teamRequestRepository).deleteById(userUuid);
        }

        @Test
        void answerTeamRequest_WithDenyRequest_ShouldDeleteRequest() {
            UUID teamUuid = UUID.fromString("91b179f6-154a-4449-8709-616d1b58f7f3");
            UUID userUuid = UUID.fromString("f30595c9-8c6b-4273-8245-9d6fd39d16f2");
            TeamEntity team = new TeamEntity();
            when(teamRequestRepository.findFirstByUserUuidAndTeamUuid(userUuid, teamUuid)).thenReturn(Optional.of(new TeamRequestEntity()));

            teamRequestService.answerTeamRequest(teamUuid, new AnswerTeamRequestReqDto(userUuid, false));

            verifyNoInteractions(userService);
            verify(teamRequestRepository).deleteById(userUuid);
        }

    }

}