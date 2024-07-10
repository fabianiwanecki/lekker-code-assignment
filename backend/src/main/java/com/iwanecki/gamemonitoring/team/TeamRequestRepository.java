package com.iwanecki.gamemonitoring.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRequestRepository extends JpaRepository<TeamRequestEntity, UUID>, PagingAndSortingRepository<TeamRequestEntity, UUID> {
    Optional<TeamRequestEntity> findFirstByUserUuidAndTeamUuid(UUID userUuid, UUID teamUuid);

    List<TeamRequestEntity> findAllByTeamUuid(UUID teamUuid);

    @Modifying
    void deleteAllByTeamUuid(UUID teamUuid);
}
