package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.authentication.ScoreService;
import com.iwanecki.gamemonitoring.authentication.SignUpReqDto;
import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ScoreService scoreService;

    public UserDto createUser(SignUpReqDto signUpReq) {
        if (userRepository.findFirstByUsername(signUpReq.username().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException("A user with this username already exists");
        }

        UserEntity userEntity = new UserEntity()
                .setUsername(signUpReq.username().toLowerCase())
                .setPassword(signUpReq.password())
                .setScore(scoreService.generateRandomScore());

        userEntity = userRepository.save(userEntity);
        return userMapper.mapEntityToDto(userEntity);
    }

    public PageDto<UserDto> listUsers(Integer page, Integer size) {

        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        Page<UserEntity> users = userRepository.findAll(pageable);

        return new PageDto<>(page, users.getContent().size(), users.getTotalElements(), userMapper.mapEntityToDto(users.getContent()));
    }

    public void addUserToTeam(UUID userUuid, TeamRole teamRole, TeamEntity team) {
        Optional<UserEntity> userOptional = userRepository.findById(userUuid);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("The user doesn't exists");
        }

        UserEntity user = userOptional.get();

        if (user.getTeam() != null) {
            throw new AlreadyTeamMemberException("The user is already member of a team");
        }

        user.setTeam(team).setTeamRole(teamRole);
        userRepository.save(user);
    }
}
