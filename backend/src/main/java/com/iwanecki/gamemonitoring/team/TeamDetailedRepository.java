package com.iwanecki.gamemonitoring.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamDetailedRepository extends JpaRepository<TeamDetailedEntity, UUID>, PagingAndSortingRepository<TeamDetailedEntity, UUID> {


}
