package com.iwanecki.gamemonitoring.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "team")
public class TeamEntity {

    @UuidGenerator
    @Id
    private UUID uuid;

    private String name;

    private Integer maxMembers;
}
