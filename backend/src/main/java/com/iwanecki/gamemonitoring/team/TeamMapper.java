package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeamMapper {

    TeamDto mapEntitytoDto(TeamEntity entity);

    TeamWithMembersDto mapEntitytoDtoWithMembers(TeamEntity entity);
    List<TeamDto> mapEntitytoDto(List<TeamEntity> entity);

}
