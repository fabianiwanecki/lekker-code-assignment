package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.team.TeamMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring", uses = TeamMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto mapUserEntityToUserDto(UserEntity entity);

    UserWithRankDto mapUserEntityAndRankToUserWithRankDto(UserEntity entity, Long rank);

    default List<UserWithRankDto> mapUserEntityAndRankToUserWithRankDto(List<UserEntity> entityList, List<Long> rankList) {
        if (rankList == null || rankList.size() != entityList.size()) {
            // Fallback mechanism: Ignore Rank if the list sizes don't match and return users without a rank value
            return mapUserEntityToUserWithRankDto(entityList);
        }

        return IntStream.range(0, rankList.size()).mapToObj(i -> mapUserEntityAndRankToUserWithRankDto(entityList.get(i), rankList.get(i) + 1)).toList();
    }

    List<UserWithRankDto> mapUserEntityToUserWithRankDto(List<UserEntity> entityList);

    UserWithRankDto mapUserEntityToUserWithRankDto(UserEntity entity);

    UserWithRankAndTeamDto mapUserEntityAndRankToUserWithRankAndTeamDto(UserEntity entity, Long rank);

    default List<UserWithRankAndTeamDto> mapUserEntityAndRankToUserWithRankAndTeamDto(List<UserEntity> entityList, List<Long> rankList) {
        if (rankList == null || rankList.size() != entityList.size()) {
            // Fallback mechanism: Ignore Rank if the list sizes don't match and return users without a rank value
            return mapUserEntityToUserWithRankAndTeamDto(entityList);
        }

        return IntStream.range(0, rankList.size()).mapToObj(i -> mapUserEntityAndRankToUserWithRankAndTeamDto(entityList.get(i), rankList.get(i) + 1)).toList();
    }

    List<UserWithRankAndTeamDto> mapUserEntityToUserWithRankAndTeamDto(List<UserEntity> entityList);
}
