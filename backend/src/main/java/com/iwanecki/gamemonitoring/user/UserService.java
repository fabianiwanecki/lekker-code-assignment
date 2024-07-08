package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.authentication.ScoreService;
import com.iwanecki.gamemonitoring.authentication.SignUpReqDto;
import com.iwanecki.gamemonitoring.shared.PageDto;
import com.iwanecki.gamemonitoring.team.TeamAlreadyFullException;
import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamRole;
import com.iwanecki.gamemonitoring.team.TeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ScoreService scoreService;
    private final PasswordEncoder passwordEncoder;
    private final UserRankRepository userRankRepository;
    private final TeamService teamService;


    @Transactional
    public UserDto createUser(SignUpReqDto signUpReq) {
        if (userRepository.findFirstByUsername(signUpReq.username().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException("A user with this username already exists");
        }

        UserEntity userEntity = new UserEntity()
                .setUsername(signUpReq.username().toLowerCase())
                .setPassword(passwordEncoder.encode(signUpReq.password()))
                .setScore(scoreService.generateRandomScore());


        userEntity = userRepository.save(userEntity);

        userRankRepository.addUserScore(userEntity.getUuid(), userEntity.getScore());

        return userMapper.mapUserEntityToUserDto(userEntity);
    }

    public List<UserWithRankDto> fetchMultipleUsersWithRank(List<UUID> userUuidList) {
        List<UserEntity> users = userRepository.findAllByUuidIn(userUuidList);

        // Recreate uuid list from db list to remove non-existent uuids from initial list
        List<UUID> userUuidListFromRepository = users.stream().map(UserEntity::getUuid).toList();

        List<Long> rankList = userRankRepository.listUserRanks(userUuidListFromRepository);

        return userMapper.mapUserEntityAndRankToUserWithRankDto(users, rankList);
    }

    public PageDto<UserWithRankAndTeamDto> listUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("score", "username"));
        Page<UserEntity> users = userRepository.findAll(pageable);

        List<UUID> userUuidList = users.getContent().stream().map(UserEntity::getUuid).toList();

        List<Long> rankList = userRankRepository.listUserRanks(userUuidList);

        return new PageDto<>(page, users.getContent().size(), users.getTotalElements(), userMapper.mapUserEntityAndRankToUserWithRankAndTeamDto(users.getContent(), rankList));
    }

    public void removeTeam(UUID teamUuid) {
        userRepository.removeTeam(teamUuid);
    }

    public void addUserToTeam(UUID userUuid, TeamRole teamRole, UUID teamUuid) {
        UserEntity user = userRepository.findById(userUuid)
                .orElseThrow(UserNotFoundException::new);

        addUserToTeam(user, teamRole, teamUuid);
    }

    public void addUserToTeam(String username, TeamRole teamRole, UUID teamUuid) {
        UserEntity user = userRepository.findFirstByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        addUserToTeam(user, teamRole, teamUuid);
    }

    public void addUserToTeam(UserEntity user, TeamRole teamRole, UUID teamUuid) {
        if (user.getTeam() != null) {
            throw new AlreadyTeamMemberException("The user is already member of a team");
        }

        TeamEntity team = teamService.fetchByUuid(teamUuid);

        if (team.getMembers().size() >= team.getMaxMembers()) {
            throw new TeamAlreadyFullException("The team is already full");
        }

        user.setTeam(team).setTeamRole(teamRole);
        userRepository.save(user);
    }

    public UserEntity fetchByUsername(String username) {
        return userRepository.findFirstByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean existsByUuid(UUID userUuid) {
        return userRepository.existsById(userUuid);
    }
}
