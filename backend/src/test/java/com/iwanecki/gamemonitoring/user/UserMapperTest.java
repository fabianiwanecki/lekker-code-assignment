package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.team.TeamDto;
import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamMapper;
import com.iwanecki.gamemonitoring.team.TeamMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    @Spy
    private TeamMapperImpl teamMapper;


    @Test
    void mapUserEntityAndRankToUserWithRankDto_WithListSizeNotEqual_ShouldMapWithoutRank() {
        UUID userUuid1 = UUID.fromString("0340c83a-d150-43af-ae32-b0a3e2ea15fa");
        UUID userUuid2 = UUID.fromString("9dd61b1b-4053-4b17-9649-c7915932315b");

        List<UserWithRankDto> actual = userMapper.mapUserEntityAndRankToUserWithRankDto(List.of(
                new UserEntity().setUuid(userUuid1).setUsername("TestUser1").setScore(12),
                new UserEntity().setUuid(userUuid2).setUsername("TestUser2").setScore(5)
        ), List.of(
                1L
        ));

        List<UserWithRankDto> expected = List.of(
                new UserWithRankDto(userUuid1, "TestUser1", 12, null),
                new UserWithRankDto(userUuid2, "TestUser2", 5, null)
        );

        assertEquals(expected, actual);
    }

    @Test
    void mapUserEntityAndRankToUserWithRankAndTeamDto_WithListSizeNotEqual_ShouldMapWithoutRank() {
        UUID userUuid1 = UUID.fromString("0340c83a-d150-43af-ae32-b0a3e2ea15fa");
        UUID userUuid2 = UUID.fromString("9dd61b1b-4053-4b17-9649-c7915932315b");
        UUID teamUuid = UUID.fromString("449d3095-8c09-4b34-94bd-ae10d418e828");

        List<UserWithRankAndTeamDto> actual = userMapper.mapUserEntityAndRankToUserWithRankAndTeamDto(List.of(
                new UserEntity().setUuid(userUuid1).setUsername("TestUser1").setScore(12).setTeam(
                        new TeamEntity().setUuid(teamUuid).setName("TestTeam").setMaxMembers(12)
                ),
                new UserEntity().setUuid(userUuid2).setUsername("TestUser2").setScore(5).setTeam(
                        new TeamEntity().setUuid(teamUuid).setName("TestTeam").setMaxMembers(12)
                )
        ), List.of(
                1L
        ));

        List<UserWithRankAndTeamDto> expected = List.of(
                new UserWithRankAndTeamDto(userUuid1, "TestUser1", 12, null, new TeamDto(teamUuid, "TestTeam", 12)),
                new UserWithRankAndTeamDto(userUuid2, "TestUser2", 5, null, new TeamDto(teamUuid, "TestTeam", 12))
        );

        assertEquals(expected, actual);
    }
}