package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.authentication.ScoreService;
import com.iwanecki.gamemonitoring.authentication.SignUpReqDto;
import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScoreService scoreService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserRankRepository userRankRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @BeforeEach
    void setup() {
        UserMapperImpl userMapper = new UserMapperImpl(new TeamMapperImpl());
        ReflectionTestUtils.setField(
                userService,
                "userMapper",
                userMapper
        );
    }

    @Nested
    class FetchByUsernameTest {

        @Test
        void fetchByUsername_WithUserNotFound_ShouldThrow() {
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.fetchByUsername("TestUser"));
        }

        @Test
        void fetchByUsername_WithValidParams_ShouldReturnUser() {
            UUID userUuid = UUID.fromString("606d2af8-cee2-4990-ad35-0b8011781044");
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.of(
                    new UserEntity().setUsername("TestUser").setScore(12).setUuid(userUuid)
            ));

            UserDto actual = userService.fetchByUsername("TestUser");
            UserDto expected = new UserDto(userUuid, "TestUser", 12, null);

            assertEquals(expected, actual);
        }

    }

    @Nested
    class FetchUserWithRankAndTeamTest {
        @Test
        void fetchUserWithRankAndTeam_WithValidParams_ShouldReturnUser() {
            UUID userUuid = UUID.fromString("606d2af8-cee2-4990-ad35-0b8011781044");
            when(userRepository.findById(userUuid)).thenReturn(Optional.of(new UserEntity()
                    .setUuid(userUuid)
                    .setUsername("TestUser")
                    .setScore(192)));
            when(userRankRepository.fetchUserRank(userUuid)).thenReturn(1L);

            UserWithRankAndTeamDto actual = userService.fetchUserWithRankAndTeam(userUuid);
            UserWithRankAndTeamDto expected = new UserWithRankAndTeamDto(userUuid, "TestUser", 192, 1L, null);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class ListUsersTest {

        @Test
        void listUsers_WithValidParameters_ShouldReturnUserList() {
            UUID userUuid1 = UUID.fromString("606d2af8-cee2-4990-ad35-0b8011781044");
            UUID userUuid2 = UUID.fromString("bd577733-e26a-4af1-9070-16d76390723b");
            UUID userUuid3 = UUID.fromString("8c26c5eb-43ef-421e-a52d-0a6ea26b3b48");
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(12);
            when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                    new UserEntity()
                            .setUuid(userUuid1)
                            .setUsername("user1")
                            .setScore(10)
                            .setTeam(team),
                    new UserEntity()
                            .setUuid(userUuid2)
                            .setUsername("user2")
                            .setScore(20)
                            .setTeam(team),
                    new UserEntity()
                            .setUuid(userUuid3)
                            .setUsername("user3")
                            .setScore(2)
                            .setTeam(team)
            ), Pageable.ofSize(3).withPage(0), 5));
//            when(redisTemplate.execute(null, List.of("user:score"), List.of(userUuid1.toString(), userUuid2.toString(), userUuid3.toString()).toArray())).thenReturn(List.of(1L, 0L, 2L));
            when(userRankRepository.listUserRanks(any())).thenReturn(List.of(1L, 0L, 2L));

            PageDto<UserWithRankAndTeamDto> actual = userService.listUsers(1, 3);

            PageDto<UserWithRankAndTeamDto> expected = new PageDto<>(1, 3, 5, List.of(
                    new UserWithRankAndTeamDto(userUuid1, "user1", 10, 2L, new TeamDto(null, "TestTeam", 12)),
                    new UserWithRankAndTeamDto(userUuid2, "user2", 20, 1L, new TeamDto(null, "TestTeam", 12)),
                    new UserWithRankAndTeamDto(userUuid3, "user3", 2, 3L, new TeamDto(null, "TestTeam", 12))
            ));

            assertEquals(expected, actual);
        }
    }

    @Nested
    class CreateUserTest {

        @Test
        void createUser_WithUserAlreadyExists_ShouldReturnException() {
            when(userRepository.findFirstByUsername("testuser")).thenReturn(Optional.of(new UserEntity()));
            SignUpReqDto signUpReq = new SignUpReqDto("testuser", "testpassword");

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(signUpReq));
        }

        @Test
        void createUser_WithUserAlreadyExistsIgnoreCase_ShouldReturnException() {
            when(userRepository.findFirstByUsername("testuser")).thenReturn(Optional.of(new UserEntity()));
            SignUpReqDto signUpReq = new SignUpReqDto("TESTUSER", "testpassword");

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(signUpReq));
        }

        @Test
        void createUser_WithValidParameters_ShouldSaveUserAndReturn() {
            UserEntity user = new UserEntity()
                    .setUuid(UUID.fromString("3fc21390-2bfd-44cc-a1c5-23847302028b"))
                    .setUsername("testuser")
                    .setScore(42);
            when(userRepository.findFirstByUsername("testuser")).thenReturn(Optional.empty());
            when(userRepository.save(any())).thenReturn(user);
            when(scoreService.generateRandomScore()).thenReturn(42);
            SignUpReqDto signUpReq = new SignUpReqDto("testuser", "testpassword");
            UserDto expected = new UserDto(UUID.fromString("3fc21390-2bfd-44cc-a1c5-23847302028b"), "testuser", 42, null);

            UserDto actual = userService.createUser(signUpReq);

            assertEquals(expected, actual);
        }

    }

    @Nested
    class AddUserToTeamTest {

        @Test
        void addUserToTeam_WithValidParams_ShouldAddUserToTeam() {
            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity()
                    .setName("TestTeam")
                    .setMaxMembers(12)
                    .setUuid(UUID.fromString("bd5e089f-d804-4721-8114-f99e55826d4e"))
                    .setMembers(List.of());;
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12);
            when(userRepository.findById(userUuid)).thenReturn(Optional.of(userEntity));

            userService.addUserToTeam(userUuid, team, TeamRole.OWNER);

            verify(userRepository).save(userEntityArgumentCaptor.capture());
            assertAll(() -> {
                assertNotNull(userEntityArgumentCaptor.getValue().getTeam());
                assertEquals(TeamRole.OWNER, userEntityArgumentCaptor.getValue().getTeamRole());
            });
        }

        @Test
        void addUserToTeam_WithTeamIsFull_ShouldThrow() {
            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity()
                    .setName("TestTeam")
                    .setMaxMembers(1)
                    .setUuid(UUID.fromString("bd5e089f-d804-4721-8114-f99e55826d4e"))
                    .setMembers(List.of(new UserEntity()));
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12);
            when(userRepository.findById(userUuid)).thenReturn(Optional.of(userEntity));

            assertThrows(TeamAlreadyFullException.class, () -> userService.addUserToTeam(userUuid, team, TeamRole.OWNER));
        }

        @Test
        void addUserToTeam_WithUserNotFound_ShouldThrow() {
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            assertThrows(UserNotFoundException.class, () -> userService.addUserToTeam(userUuid, team, TeamRole.OWNER));
        }

        @Test
        void addUserToTeamUsername_WithUserNotFound_ShouldThrow() {
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.empty());
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            assertThrows(UserNotFoundException.class, () -> userService.addUserToTeam("TestUser", team, TeamRole.OWNER));
        }

        @Test
        void addUserToTeamUsername_WithValidParams_ShouldAddUserToTeam() {
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity()
                    .setName("TestTeam")
                    .setMaxMembers(12)
                    .setUuid(UUID.fromString("bd5e089f-d804-4721-8114-f99e55826d4e"))
                    .setMembers(List.of());
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12);
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.of(userEntity));

            userService.addUserToTeam("TestUser", team, TeamRole.OWNER);

            verify(userRepository).save(userEntityArgumentCaptor.capture());
            assertAll(() -> {
                assertNotNull(userEntityArgumentCaptor.getValue().getTeam());
                assertEquals(TeamRole.OWNER, userEntityArgumentCaptor.getValue().getTeamRole());
            });
        }

        @Test
        void addUserToTeam_WithUserAlreadyInATeam_ShouldThrow() {
            UUID teamUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12).setTeam(new TeamEntity()).setTeamRole(TeamRole.OWNER);
            when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            assertThrows(AlreadyTeamMemberException.class, () -> userService.addUserToTeam(userUuid, team, TeamRole.OWNER));
        }

    }

    @Nested
    class FetchMultipleUsersWithRankTest {

        @Test
        void fetchMultipleUsersWithRank_WithValidParams_ShouldFetchUsers() {
            UUID userUuid1 = UUID.fromString("606d2af8-cee2-4990-ad35-0b8011781044");
            UUID userUuid2 = UUID.fromString("bd577733-e26a-4af1-9070-16d76390723b");
            UUID userUuid3 = UUID.fromString("8c26c5eb-43ef-421e-a52d-0a6ea26b3b48");
            when(userRepository.findAllByUuidIn(any())).thenReturn(List.of(
                    new UserEntity().setUuid(userUuid1).setUsername("TestUser1").setScore(1042),
                    new UserEntity().setUuid(userUuid3).setUsername("TestUser3").setScore(19)
            ));
            when(userRankRepository.listUserRanks(List.of(userUuid1, userUuid3))).thenReturn(List.of(2L, 4L));

            List<UserWithRankDto> actual = userService.fetchMultipleUsersWithRank(List.of(userUuid1, userUuid2, userUuid3));

            List<UserWithRankDto> expected = List.of(
                    new UserWithRankDto(userUuid1, "TestUser1", 1042, 3L),
                    new UserWithRankDto(userUuid3, "TestUser3", 19, 5L)
            );

            assertEquals(expected, actual);
        }

    }

}