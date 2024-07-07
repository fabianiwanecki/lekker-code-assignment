package com.iwanecki.gamemonitoring.team;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto mapEntitytoDto(TeamEntity entity);
    List<TeamDto> mapEntitytoDto(List<TeamEntity> entity);

}
