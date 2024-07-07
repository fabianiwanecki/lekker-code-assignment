package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.user.UserEntity;
import com.iwanecki.gamemonitoring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getTeam() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getTeamRole() + "_" + user.getTeam().getUuid()));
        }

        return new User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}
