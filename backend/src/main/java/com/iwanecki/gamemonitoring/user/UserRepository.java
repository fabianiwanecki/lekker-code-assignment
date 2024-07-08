package com.iwanecki.gamemonitoring.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, PagingAndSortingRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByUsername(String username);

    List<UserEntity> findAllByUuidIn(List<UUID> uuidList);

    @Modifying
    @Query("update UserEntity u set u.teamRole = null, u.team = null where u.team.uuid = :teamUuid")
    void removeTeam(UUID teamUuid);

}
