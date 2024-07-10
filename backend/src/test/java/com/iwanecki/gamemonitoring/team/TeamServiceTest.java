package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.user.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    private TeamDetailedRepository teamDetailedRepository;

    @Mock
    private UserService userService;

    @Spy
    private TeamMapperImpl teamMapper;

    @Nested
    class AddUserToTeamTest {

        @Test
        void addUserToTeam_WithValidParams_ShouldAddUserToTeam() {
            UUID userUuid = UUID.fromString("238efcc7-88b3-457d-aa03-63122aeb047a");
            UUID teamUuid = UUID.fromString("97932fe1-80a1-4fd6-889c-4ea6a6d3ed2d");
            TeamEntity team = new TeamEntity().setUuid(teamUuid);
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.of(team));

            teamService.addUserToTeam(userUuid, TeamRole.OWNER, teamUuid);

            verify(userService).addUserToTeam(userUuid, team, TeamRole.OWNER);
        }

        @Test
        void addUserToTeam_WithTeamNotFound_ShouldThrow() {
            UUID userUuid = UUID.fromString("238efcc7-88b3-457d-aa03-63122aeb047a");
            UUID teamUuid = UUID.fromString("97932fe1-80a1-4fd6-889c-4ea6a6d3ed2d");
            when(teamRepository.findById(teamUuid)).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class, () -> teamService.addUserToTeam(userUuid, TeamRole.OWNER, teamUuid));
        }

    }

    @Nested
    class CreateTeamTest {

        @Test
        void createTeam_WithValidParams_ShouldReturnTeam() {
            when(teamRepository.save(any())).then(returnsFirstArg());
            TeamDto actual = teamService.createTeam(new CreateTeamReqDto("TestTeam", 10), "TestUser");

            TeamDto expected = new TeamDto(null, "TestTeam", 10);

            assertEquals(expected, actual);
        }

        @Test
        void createTeam_WithValidParams_ShouldAddUserToTeam() {
            UUID teamUuid = UUID.fromString("d6055f3d-7c62-4449-ad1e-794e2410cd10");
            TeamEntity team = new TeamEntity().setUuid(teamUuid).setName("TestTeam").setMaxMembers(10);
            when(teamRepository.save(any())).thenReturn(team);

            teamService.createTeam(new CreateTeamReqDto("TestTeam", 10), "TestUser");

            verify(userService).addUserToTeam("TestUser", team, TeamRole.OWNER);
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
            when(teamDetailedRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                    new TeamDetailedEntity().setName("Team1").setMaxMembers(10),
                    new TeamDetailedEntity().setName("Team2").setMaxMembers(20),
                    new TeamDetailedEntity().setName("Team3").setMaxMembers(14)
            ), Pageable.ofSize(3).withPage(0), 5));

            PageDto<TeamDetailedDto> actual = teamService.listTeams(1, 3);

            PageDto<TeamDetailedDto> expected = new PageDto<>(1, 3, 5, List.of(
                    new TeamDetailedDto(null, "Team1", 10, null, null, null),
                    new TeamDetailedDto(null, "Team2", 20, null, null, null),
                    new TeamDetailedDto(null, "Team3", 14, null, null, null)
            ));

            assertEquals(expected, actual);
        }
    }

    @Nested
    class FetchTeamDetailsTest {

        @Test
        void fetchTeamDetails_WithValidParameters_ShouldReturnTeamDetails() {
            UUID teamUuid = UUID.fromString("366c91cb-be63-40bc-be7e-7105aefc7efc");
            when(teamDetailedRepository.findById(teamUuid)).thenReturn(Optional.of(new TeamDetailedEntity()
                    .setName("TestTeam")
                    .setMaxMembers(12)
                    .setCurrentMembers(1)
                    .setOwner("TestUser")
                    .setTotalScore(144L)
                    .setMembers(List.of(new UserEntity()
                            .setUsername("TestUser")
                            .setScore(144)))));
            when(userService.fetchMultipleUsersWithRank(any())).thenReturn(List.of(new UserWithRankDto(null, "TestUser", 144, 1L)));

            TeamDetailedWithMembersDto actual = teamService.fetchTeamDetails(teamUuid);

            TeamDetailedWithMembersDto expected = new TeamDetailedWithMembersDto(null, "TestTeam", 12, 1, "TestUser", 144L, List.of(new UserWithRankDto(null, "TestUser", 144, 1L)));

            assertEquals(expected, actual);
        }

        @Test
        void fetchTeamDetails_WithTeamNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("366c91cb-be63-40bc-be7e-7105aefc7efc");
            when(teamDetailedRepository.findById(teamUuid)).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class, () -> teamService.fetchTeamDetails(teamUuid));
        }
    }
}