package com.iwanecki.gamemonitoring.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private String jwtSecret = "testsecrettestsecrettestsecrettestsecret";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", Integer.MAX_VALUE);
    }

    @Nested
    class GenerateJwtTokenTest {

        @Test
        void generateJwtToken_WithValidParams_ShouldGenerateJwt() {
            String actual = jwtUtils.generateJwtToken(new TestingAuthenticationToken(new User("TestUser", "TestPassword", Collections.emptyList()), null));
            assertNotNull(actual);
        }
    }

    @Nested
    class GetUserNameFromJwtTokenTest {
        @Test
        void getUserNameFromJwtToken_WithValidParams_ShouldReturnUsername() {
            String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlciIsInRlYW0iOltdLCJpYXQiOjE3MjAzNzIwMDQsImV4cCI6OTk3MjI1MTk0ODh9.iC6pchbdo-0pm3VR5huE70P-tqnSdkjXqwjsXdJRIaE";

            String actual = jwtUtils.getUserNameFromJwtToken(jwt);

            assertEquals("TestUser", actual);
        }
    }

    @Nested
    class ValidateJwtTokenTest {

        @Test
        void validateJwtToken_WithValidToken_ShouldReturnTrue() {
            String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlciIsInRlYW0iOltdLCJpYXQiOjE3MjAzNzIwMDQsImV4cCI6OTk3MjI1MTk0ODh9.iC6pchbdo-0pm3VR5huE70P-tqnSdkjXqwjsXdJRIaE";

            boolean actual = jwtUtils.validateJwtToken(jwt);

            assertTrue(actual);
        }

        @Test
        void validateJwtToken_WithInvalidToken_ShouldReturnFalse() {
            String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlciIsInRlYW0iOltdLCJpYXQiOjE3MjAzNzIwMDQsImV4cCI6OTk3MjI1MTk0ODh9.sXlVIw1IEy4-36sLasI-xICs132N5ZRymbn2poN0qgQ";

            boolean actual = jwtUtils.validateJwtToken(jwt);

            assertFalse(actual);
        }
    }

}