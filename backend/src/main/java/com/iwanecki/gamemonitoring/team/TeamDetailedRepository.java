package com.iwanecki.gamemonitoring.team;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamDetailedRepository extends PagingAndSortingRepository<TeamDetailedEntity, UUID> {


}
