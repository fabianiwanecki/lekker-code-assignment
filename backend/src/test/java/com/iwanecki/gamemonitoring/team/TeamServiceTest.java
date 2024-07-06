package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Spy
    private TeamMapperImpl teamMapper;

    @Nested
    class CreateTeamTest {

        @Test
        void createTeam_WithValidParams_ShouldReturnTeam() {
            when(teamRepository.save(any())).then(returnsFirstArg());
            TeamDto actual = teamService.createTeam(new CreateTeamReqDto(UUID.fromString("3f79686b-9e99-4671-9d69-488fa9e0778d"), "TestTeam", 10));

            TeamDto expected = new TeamDto(null, "TestTeam", 10);

            assertEquals(expected, actual);
        }

        @Test
        void createTeam_WithValidParams_ShouldAddUserToTeam() {
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            when(teamRepository.save(any())).thenReturn(team);

            teamService.createTeam(new CreateTeamReqDto(UUID.fromString("3f79686b-9e99-4671-9d69-488fa9e0778d"), "TestTeam", 10));

            verify(userService).addUserToTeam(UUID.fromString("3f79686b-9e99-4671-9d69-488fa9e0778d"), TeamRole.OWNER, team);
        }
    }

    @Nested
    class DeleteTeamTest {

        @Test
        void deleteTeam_WithTeamNotFound_ShouldThrow() {
            when(teamRepository.findById(any())).thenReturn(Optional.empty());

            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(teamUuid));
        }

        @Test
        void deleteTeam_WithValidParameters_ShouldDeleteTeam() {
            when(teamRepository.findById(any())).thenReturn(Optional.of(new TeamEntity()));

            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            teamService.deleteTeam(teamUuid);

            verify(teamRepository).deleteById(teamUuid);
        }

        @Test
        void deleteTeam_WithValidParameters_ShouldRemoveTeamFromUsers() {
            when(teamRepository.findById(any())).thenReturn(Optional.of(new TeamEntity()));

            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            teamService.deleteTeam(teamUuid);

            verify(userService).removeTeam(teamUuid);
        }

    }
}