package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.team.model.TeamDetailedDto;
import com.iwanecki.gamemonitoring.team.model.TeamDetailedWithMembersDto;
import com.iwanecki.gamemonitoring.user.model.UserWithRankDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDetailedDto mapEntitytoDtoDetailed(TeamDetailedEntity entity);
    List<TeamDetailedDto> mapEntitytoDtoDetailed(List<TeamDetailedEntity> entity);

    @Mapping(source = "members", target = "members")
    TeamDetailedWithMembersDto mapEntitytoDtoWithMembers(TeamDetailedEntity entity, List<UserWithRankDto> members);

    TeamDto mapEntitytoDto(TeamEntity entity);
    List<TeamDto> mapEntitytoDto(List<TeamEntity> entity);
}
