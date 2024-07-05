package com.iwanecki.gamemonitoring.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;

    public UserDto signUp(SignUpReqDto signUpReq) {
        return userService.createUser(signUpReq);
    }
}
