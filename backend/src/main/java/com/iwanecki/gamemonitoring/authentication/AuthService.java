package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.user.UserDto;
import com.iwanecki.gamemonitoring.user.UserService;
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
