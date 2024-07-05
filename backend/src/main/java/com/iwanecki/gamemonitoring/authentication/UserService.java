package com.iwanecki.gamemonitoring.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(SignUpReqDto signUpReq) {
        if (userRepository.findFirstByUsername(signUpReq.username().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException("A user with this username already exists");
        }

        UserEntity userEntity = new UserEntity()
                .setUsername(signUpReq.username().toLowerCase())
                .setPassword(signUpReq.password());

        userEntity = userRepository.save(userEntity);
        return userMapper.mapEntityToDto(userEntity);
    }
}
