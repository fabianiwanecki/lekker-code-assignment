package com.iwanecki.gamemonitoring.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "team_request")
public class TeamRequestEntity {

    @Id
    private UUID userUuid;

    private UUID teamUuid;

}
