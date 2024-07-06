package com.iwanecki.gamemonitoring.user;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapEntityToDto(UserEntity entity);

    List<UserDto> mapEntityToDto(List<UserEntity> content);
}
