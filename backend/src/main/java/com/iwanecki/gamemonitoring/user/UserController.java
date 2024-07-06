package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.shared.PageDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public PageDto<UserDto> listUsers(@Min(1) @RequestParam Integer page, @Min(1) @Max(100) @RequestParam Integer size) {
        return userService.listUsers(page, size);
    }
}
