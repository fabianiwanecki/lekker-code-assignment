package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.team.exception.TeamNotFoundException;
import com.iwanecki.gamemonitoring.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTeamServiceTest {

    @InjectMocks
    private DeleteTeamService deleteTeamService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private TeamRequestService teamRequestService;

    @Nested
    class DeleteTeamTest {

        @Test
        void deleteTeam_WithTeamNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            when(teamService.existsByUuid(teamUuid)).thenReturn(false);

            assertThrows(TeamNotFoundException.class, () -> deleteTeamService.deleteTeam(teamUuid));
        }

        @Test
        void deleteTeam_WithValidParameters_ShouldDeleteTeam() {
            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            when(teamService.existsByUuid(any())).thenReturn(true);

            deleteTeamService.deleteTeam(teamUuid);

            verify(teamService).deleteByUuid(teamUuid);
            verify(userService).removeUsersFromTeam(teamUuid);
            verify(teamRequestService).deleteAllRequests(teamUuid);
        }
    }
}