package com.iwanecki.gamemonitoring.authentication;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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