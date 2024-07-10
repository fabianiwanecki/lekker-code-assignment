package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.authentication.model.SignInReqDto;
import com.iwanecki.gamemonitoring.authentication.model.SignInResDto;
import com.iwanecki.gamemonitoring.user.model.UserDto;
import com.iwanecki.gamemonitoring.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Nested
    class SignInTest {

        @Test
        void signIn_WithValidParams_ShouldReturnJwt() {
            SignInResDto expected = new SignInResDto("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyZW5jcnlwdGVkcHciLCJ0ZWFtIjpbeyJhdXRob3JpdHkiOiJPV05FUl81MzE4ZGUxNC0xYTJhLTQzYWYtOGFjOC0yNDM3YWIzODBjNjcifV0sImlhdCI6MTcyMDM2OTg1MCwiZXhwIjoxNzIwMzY5ODUwfQ.09iwyr8yFt40_Mdcc9IiTkjqtM0vGniquIWnsFSUaIlTTNj1BJJh2IDDwdQ4lmhzYp7gTlqof_HjZmzZJlV4QA", null);
            when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(null, null));
            when(jwtUtils.generateJwtToken(any())).thenReturn(expected.token());
            when(userService.fetchByUsername("TestUser")).thenReturn(new UserDto(null, "TestUser", 12, null));
            SignInResDto actual = authService.signIn(new SignInReqDto("TestUser", "TestPw"));

            assertEquals(expected, actual);
        }

    }

}