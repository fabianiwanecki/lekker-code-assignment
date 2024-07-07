package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamRole;
import com.iwanecki.gamemonitoring.user.UserEntity;
import com.iwanecki.gamemonitoring.user.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Nested
    class LoadUserByUsernameTest {

        @Test
        void loadUserByUsername_WithUserNotFound_ShouldThrow() {
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("TestUser"));
        }

        @Test
        void loadUserByUsername_WithUserHasTeam_ShouldAddGrantedAuthority() {
            UUID teamUuid = UUID.fromString("9bb4ee09-9657-47eb-b112-af0a816c5729");
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.of(
                    new UserEntity().setUsername("TestUser").setPassword("TestTest").setTeamRole(TeamRole.OWNER).setTeam(
                            new TeamEntity().setUuid(teamUuid)
                    )
            ));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername("TestUser");

            assertEquals(new SimpleGrantedAuthority("OWNER_9bb4ee09-9657-47eb-b112-af0a816c5729"), userDetails.getAuthorities().toArray()[0]);
        }

        @Test
        void loadUserByUsername_WithUserHasNoTeam_ShouldNotAddGrantedAuthority() {
            when(userRepository.findFirstByUsername("TestUser")).thenReturn(Optional.of(
                    new UserEntity().setUsername("TestUser").setPassword("TestTest")
            ));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername("TestUser");

            assertTrue(userDetails.getAuthorities().isEmpty());
        }

    }

}