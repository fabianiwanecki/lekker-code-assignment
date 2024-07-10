package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.user.UserDto;
import com.iwanecki.gamemonitoring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserDto signUp(SignUpReqDto signUpReq) {
        return userService.createUser(signUpReq);
    }

    public SignInResDto signIn(SignInReqDto signInReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInReq.username(), signInReq.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDto user = userService.fetchByUsername(signInReq.username());

        return new SignInResDto(jwt, user.uuid());
    }
}
