package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}