package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.user.UserDto;
import com.iwanecki.gamemonitoring.user.UserEntity;
import com.iwanecki.gamemonitoring.user.UserMapperImpl;
import com.iwanecki.gamemonitoring.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    private TeamMapperImpl teamMapper;

    @BeforeEach
    void setup() {
        teamMapper = new TeamMapperImpl(new UserMapperImpl());
        ReflectionTestUtils.setField(
                teamService,
                "teamMapper",
                teamMapper
        );
    }

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

    @Nested
    class UpdateTeamTest {

        @Test
        void updateTeam_WithTeamNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("c0dc7fad-89c1-4237-a550-1b9a6ea0e0c5");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.empty());

            UpdateTeamReqDto updateTeamReq = new UpdateTeamReqDto("TestTeam1", null);
            assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(teamUuid, updateTeamReq));
        }

        @Test
        void updateTeam_WithMaxMembersLowerThanCurrentMembers_ShouldThrow() {
            UUID teamUuid = UUID.fromString("c0dc7fad-89c1-4237-a550-1b9a6ea0e0c5");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.of(new TeamEntity().setMembers(List.of(new UserEntity(), new UserEntity()))));

            UpdateTeamReqDto updateTeamReq = new UpdateTeamReqDto(null, 1);
            assertThrows(TeamUpdateException.class, () -> teamService.updateTeam(teamUuid, updateTeamReq));
        }

        @Test
        void updateTeam_WithValidParams_ShouldUpdateNameAndMaxMembers() {
            UUID teamUuid = UUID.fromString("c0dc7fad-89c1-4237-a550-1b9a6ea0e0c5");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.of(new TeamEntity().setMembers(List.of(new UserEntity(), new UserEntity()))));
            when(teamRepository.save(any())).then(returnsFirstArg());

            UpdateTeamReqDto updateTeamReq = new UpdateTeamReqDto("TestTeam", 4);
            TeamDto actual = teamService.updateTeam(teamUuid, updateTeamReq);

            TeamDto expected = new TeamDto(null, "TestTeam", 4);

            assertEquals(expected, actual);
        }

    }

    @Nested
    class ListTeamsTest {

        @Test
        void listTeams_WithValidParameters_ShouldReturnTeamsList() {
            when(teamRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                    new TeamEntity().setName("Team1").setMaxMembers(10),
                    new TeamEntity().setName("Team2").setMaxMembers(20),
                    new TeamEntity().setName("Team3").setMaxMembers(14)
            ), Pageable.ofSize(3).withPage(0), 5));

            PageDto<TeamDto> actual = teamService.listTeams(1, 3);

            PageDto<TeamDto> expected = new PageDto<>(1, 3, 5, List.of(
                    new TeamDto(null, "Team1", 10),
                    new TeamDto(null, "Team2", 20),
                    new TeamDto(null, "Team3", 14)
            ));

            assertEquals(expected, actual);
        }
    }

    @Nested
    class FetchTeamDetailsTest {

        @Test
        void fetchTeamDetails_WithValidParameters_ShouldReturnTeamDetails() {
            UUID teamUuid = UUID.fromString("366c91cb-be63-40bc-be7e-7105aefc7efc");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.of(new TeamEntity()
                    .setName("TestTeam")
                    .setMaxMembers(12)
                    .setMembers(List.of(new UserEntity()
                            .setUsername("TestUser")
                            .setScore(144)))));

            TeamWithMembersDto actual = teamService.fetchTeamDetails(teamUuid);

            TeamWithMembersDto expected = new TeamWithMembersDto(null, "TestTeam", 12, List.of(new UserDto(null, "TestUser", 144)));

            assertEquals(expected, actual);
        }
        @Test
        void fetchTeamDetails_WithTeamNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("366c91cb-be63-40bc-be7e-7105aefc7efc");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class, () -> teamService.fetchTeamDetails(teamUuid));
        }
    }
}