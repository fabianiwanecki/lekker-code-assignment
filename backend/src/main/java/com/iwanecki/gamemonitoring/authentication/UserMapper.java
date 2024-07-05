package com.iwanecki.gamemonitoring.authentication;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapEntityToDto(UserEntity entity);

}
