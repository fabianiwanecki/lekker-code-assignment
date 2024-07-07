package com.iwanecki.gamemonitoring.authentication;

import com.iwanecki.gamemonitoring.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-up")
    public UserDto signUp(@Valid @RequestBody SignUpReqDto signUpReq) {
        return authService.signUp(signUpReq);
    }

    @PostMapping("sign-in")
    public SignInResDto signin(@Valid @RequestBody SignInReqDto signInReq) {
        return authService.signIn(signInReq);
    }
}
