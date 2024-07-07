package com.iwanecki.gamemonitoring.configuration;

import com.iwanecki.gamemonitoring.authentication.CustomUserDetailsService;
import com.iwanecki.gamemonitoring.authentication.JwtUtils;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void doFilterInternal_WithValidBearerToken_ShouldSetSecurityContext() throws ServletException, IOException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer fdslfnsk");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new User("TestUser", "TestPW", Collections.emptyList()));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext).setAuthentication(any());
    }
    @Test
    void doFilterInternal_WithNoAuthorizationHeader_ShouldNotSetSecurityContext() throws ServletException, IOException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(securityContext);
    }
    @Test
    void doFilterInternal_WithNonBearerAuthorizationHeader_ShouldNotSetSecurityContext() throws ServletException, IOException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "NonBearerToken hsjfdhsk");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(securityContext);
    }
    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetSecurityContext() throws ServletException, IOException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer hsjfdhsk");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        when(jwtUtils.validateJwtToken(any())).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(securityContext);
    }
}