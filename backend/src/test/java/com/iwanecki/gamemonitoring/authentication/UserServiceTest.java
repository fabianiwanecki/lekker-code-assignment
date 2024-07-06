package com.iwanecki.gamemonitoring.authentication;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
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

    @Nested
    class SignUpTest {

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

    }

}