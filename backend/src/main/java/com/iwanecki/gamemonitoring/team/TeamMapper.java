package com.iwanecki.gamemonitoring.team;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto mapEntitytoDto(TeamEntity entity);

}
