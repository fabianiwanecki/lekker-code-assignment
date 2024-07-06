package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.authentication.ScoreService;
import com.iwanecki.gamemonitoring.authentication.SignUpReqDto;
import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Spy
    private UserMapperImpl userMapper;

    @Captor
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor;


    @Nested
    class ListUsersTest {

        @Test
        void listUsers_WithValidParameters_ShouldReturnUserList() {
            when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                    new UserEntity().setUsername("user1").setScore(10),
                    new UserEntity().setUsername("user2").setScore(20),
                    new UserEntity().setUsername("user3").setScore(2)
            ), Pageable.ofSize(3).withPage(0), 5));

            PageDto<UserDto> actual = userService.listUsers(1, 3);

            PageDto<UserDto> expected = new PageDto<>(1, 3, 5, List.of(
                    new UserDto(null, "user1", 10),
                    new UserDto(null, "user2", 20),
                    new UserDto(null, "user3", 2)
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
            when(userRepository.findFirstByUsername("testuser")).thenReturn(Optional.empty());
            when(userRepository.save(any())).then(returnsFirstArg());
            when(scoreService.generateRandomScore()).thenReturn(42);
            SignUpReqDto signUpReq = new SignUpReqDto("testuser", "testpassword");
            UserDto expected = new UserDto(null, "testuser", 42);

            UserDto actual = userService.createUser(signUpReq);

            assertEquals(expected, actual);
        }

    }

    @Nested
    class AddUserToTeamTest {

        @Test
        void addUserToTeam_WithValidParams_ShouldAddUserToTeam() {
            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12);
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(12).setUuid(UUID.fromString("bd5e089f-d804-4721-8114-f99e55826d4e"));
            when(userRepository.findById(userUuid)).thenReturn(Optional.of(userEntity));

            userService.addUserToTeam(userUuid, TeamRole.OWNER, team);

            verify(userRepository).save(userEntityArgumentCaptor.capture());
            assertAll(() -> {
                assertNotNull(userEntityArgumentCaptor.getValue().getTeam());
                assertEquals(TeamRole.OWNER, userEntityArgumentCaptor.getValue().getTeamRole());
            });
        }

        @Test
        void addUserToTeam_WithUserNotFound_ShouldThrow() {
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            assertThrows(UserNotFoundException.class, () -> userService.addUserToTeam(userUuid, TeamRole.OWNER, team));
        }

        @Test
        void addUserToTeam_WithUserAlreadyInATeam_ShouldThrow() {
            UserEntity userEntity = new UserEntity().setUsername("TestUser").setPassword("Test").setScore(12).setTeam(new TeamEntity()).setTeamRole(TeamRole.OWNER);
            when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

            UUID userUuid = UUID.fromString("d6bfec9b-2d61-4f0e-8f8e-1b8b80ddc6a9");
            TeamEntity team = new TeamEntity().setName("TestTeam").setMaxMembers(10);
            assertThrows(AlreadyTeamMemberException.class, () -> userService.addUserToTeam(userUuid, TeamRole.OWNER, team));
        }

    }

}