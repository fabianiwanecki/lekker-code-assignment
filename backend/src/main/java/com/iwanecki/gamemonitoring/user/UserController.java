package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.user.model.UserWithRankAndTeamDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public PageDto<UserWithRankAndTeamDto> listUsers(@Min(1) @RequestParam Integer page, @Min(1) @Max(100) @RequestParam Integer size) {
        return userService.listUsers(page, size);
    }

    @GetMapping("{uuid}")
    public UserWithRankAndTeamDto fetchUser(@PathVariable UUID uuid) {
        return userService.fetchUserWithRankAndTeam(uuid);
    }
}
